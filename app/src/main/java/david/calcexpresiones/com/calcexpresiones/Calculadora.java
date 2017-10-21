package david.calcexpresiones.com.calcexpresiones;

import android.support.annotation.IntegerRes;

import java.lang.reflect.Array;
import java.util.Stack;

public class Calculadora {

    int[][] matrizPrioridad = new int[7][7];

    String[] solucionar(String strExpresion) {
        String [] error  = new String[2];
        if(strExpresion.length() ==0){
            error[0] = "Error en la expresion";
            error[1] = "Error en la expresion";
            return error;
        }
        llenarMatrizPrioridad();
        char[] infijo = new char[strExpresion.length()+2];
        char[] postfijo = new char[strExpresion.length()+2];
        String[] operandos = new String[strExpresion.length()+2];
        char[] operandosPostfijo = new char[strExpresion.length()+2];
        double[] valoresPostfijo = new double[strExpresion.length()+2];
        int err = partirExpresionInfijo(strExpresion, infijo, operandos);
        if (err==0){
            error[0] = "Error en la expresion";
            error[1] = "Error en la expresion";
            return error;
        }
        infijoAPostfijo(infijo, postfijo);
        armarExpresionPostfijo(postfijo, operandosPostfijo, valoresPostfijo, operandos);
        double result = evaluarExpresionPostfijo(operandosPostfijo, valoresPostfijo);

        String [] res =  new String[2];
        if(result == -1.0 || err==0){
            error[0] = "Error en la expresion";
            error[1] = "Error en la expresion";
            return error;
        }else{
            String postfijoCompl = armarPost(postfijo, operandos);
            res[0] = Double.toString(result);
            res[1] = "La expresion en Postfijo es: " + String.valueOf(postfijoCompl);
            return res;
        }

    }

    public String armarPost(char[] postfijo, String[] operandos) {
        char[] post = postfijo;
        String strFinal;
        String tempchar;
        char tempdouble;
        for (int i=0; i<postfijo.length; i++){
            if((post[i] != '+' && post[i] != '-' && post[i] != '*' && post[i] != '/' && post[i] != '^') && post[i] != 0){
                tempdouble = post[i];
                tempchar = operandos[Integer.valueOf(tempdouble)-48];
                post[i] = tempchar.charAt(0);
            }
        }
        strFinal = String.valueOf(post);
        return strFinal;
    }

    void llenarMatrizPrioridad() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 2 && j == 3) {
                    this.matrizPrioridad[2][3] = 1;
                } else {
                    if (i < 5 && j <= 5) {
                        if (j <= i) {
                            this.matrizPrioridad[i][j] = 1;
                        } else {
                            this.matrizPrioridad[i][j] = 0;
                        }
                    } else {
                        this.matrizPrioridad[5][j] = 0;
                        this.matrizPrioridad[j][5] = 0;
                        this.matrizPrioridad[j][6] = 1;
                    }
                }
                if (i == 0 && j == 1) {
                    this.matrizPrioridad[0][1] = 1;
                }
            }
        }
        this.matrizPrioridad[5][6] = 0;
    }

    int partirExpresionInfijo(String linea, char[] infijo, String[] operandos) {
        int k=0;
        int m1 = 0;
        int i = 0;
        for(int m=0; m<linea.length()-1; m++){
            char c = linea.charAt(m);
            if ((c!='+' && c!='-' && c!='*' && c!='/' && c!='^' && c!='(' && c!=')') && (c<'0' || c>'9')){
                return 0;
            }
        }
        while (i < linea.length()) {
            int j = 0;
            StringBuffer auxiliar = new StringBuffer();
            do {
                if ((linea.charAt(i) < '0' || linea.charAt(i) > '9') && linea.charAt(i) != '.') {
                    break;
                }
                auxiliar = auxiliar.append(linea.charAt(i));
                j++;
                i++;
            } while (i != linea.length());
            if (j != 0) {
                infijo[k] = (char) (m1 + 48);
                k++;
                operandos[m1] = auxiliar.toString();
                m1++;
            }
            if (i < linea.length()) {
                infijo[k] = linea.charAt(i);
                k++;
                i++;

            }
        }
        infijo[k] = '#';
        return 1;

    }


    int operando(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == ')' || c == '(') {
            return 1;
        }
        return 0;
    }

    //postfijoVl guarda los valores numericos de los numeros y las operaciones (ASCII)
    //posfijoOP guarda una mascara definiendo en cada posicion si va un valor o una operacion
    void armarExpresionPostfijo(char[] p, char[] operandosPostfijo, double[] valoresPostfijo, String[] operan) {
        int j = 0;
        int i = 0;
        while (p[i] != '\u0000') {
            if (operando(p[i]) == 0) {
                operandosPostfijo[j] = 'v';
                if (p[i] == '}') {
                    valoresPostfijo[j] = 0.0d;
                } else {
                    valoresPostfijo[j] = Double.parseDouble(operan[p[i] - 48]);
                }
            } else {
                operandosPostfijo[j] = 'r';
                valoresPostfijo[j] = (double) p[i];
            }
            i++;
            j++;
        }
        operandosPostfijo[j] = 'r';
        valoresPostfijo[j] = 35.0d;
    }

    //utliiza la expresion en postfijo para hacer una pila con los numeros e ir
    //comprobando si es numero o operador para mandar la operacion a EvaluarADos y retornar
    //el valor de la operacion para volverlo a poner en la pila y asi sucesivamente

    double evaluarExpresionPostfijo(char[] operandosPostfijo, double[] valoresPostfijo) {
        int i = 0;
        Stack pila = new Stack();
        while (true) {
            if (valoresPostfijo[i] == 35.0d && operandosPostfijo[i] == 'r') {
                break;
            }
            if (operandosPostfijo[i] == 'v') {
                pila.push(valoresPostfijo[i]);
            } else {
                double b = ((Double) pila.pop());
                if (pila.empty()) {
                    return -1.0;
                }
                int[] err = new int[]{0};
                double r = EvaluarADos(((Double) pila.pop()), b, (char) ((int) valoresPostfijo[i]), err);

                pila.push(r);
            }
            i++;
        }
        if (pila.size() <= 1) {
            return ((Double) pila.pop());
        }
        return -1.0;
    }

    double EvaluarADos(double a, double b, char op, int[] err) {
        double r = 0.0d;
        switch (op) {
            case '*':
                r = a * b;
                break;
            case '+':
                r = a + b;
                break;
            case '-':
                r = a - b;
                break;
            case '/':
                if (Math.abs(b) >= 1.0E-9d) {
                    r = a / b;
                    break;
                }
                System.out.println("division en 0");
                err[0] = 1;
                return 0.0d;
            case '^':
                r = Math.pow(a, b);
                break;
            default:
                err[0] = 1;
                break;
        }
        return r;
    }

    void infijoAPostfijo(char[] infijo, char[] postfijo) {
        int caracterAnterior = 0;
        int i = 0;
        int j = 0;
        Stack pila = new Stack();
        while (infijo[i] != '#') {
            if (operando(infijo[i]) == 0) {
                caracterAnterior = infijo[i];
                postfijo[j] = infijo[i];
                i++;
                j++;
            } else {
                if (caracterAnterior == 40 && infijo[i] == '-') {
                    postfijo[j] = '}';
                    j++;
                }
                while (!pila.empty() && prioridad(((Character) pila.peek()).charValue(), infijo[i]) == 1) {
                    postfijo[j] = ((Character) pila.pop()).charValue();
                    j++;
                }
                if (infijo[i] != ')') {
                    pila.push(Character.valueOf(infijo[i]));
                } else if (pila.empty()) {
                    System.out.println("error");
                } else if (((Character) pila.pop()).charValue() != '(') {
                    System.out.println("error");
                }
                caracterAnterior = infijo[i];
                i++;
            }
        }
        while (!pila.empty()) {
            postfijo[j] = (char) pila.pop();
            j++;
        }
    }

    //define la fila y columna de la matriz de valores dependiendo de los caracteres
    int prioridad(char op1, char op2) {
        int j;
        int i;
        switch (op1) {
            case '(':
                i = 5;
                break;
            case '*':
                i = 2;
                break;
            case '+':
                i = 0;
                break;
            case '-':
                i = 1;
                break;
            case '/':
                i = 3;
                break;
            case '^':
                i = 4;
                break;
            default:
                return -1;
        }
        switch (op2) {
            case '(':
                j = 5;
                break;
            case ')':
                j = 6;
                break;
            case '*':
                j = 2;
                break;
            case '+':
                j = 0;
                break;
            case '-':
                j = 1;
                break;
            case '/':
                j = 3;
                break;
            case '^':
                j = 4;
                break;
            default:
                return -1;
        }
        return this.matrizPrioridad[i][j];
    }

}
