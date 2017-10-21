package david.calcexpresiones.com.calcexpresiones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import david.calcexpresiones.com.calcexpresiones.Calculadora;

public class MainActivity extends AppCompatActivity {

    private EditText txtExpresion;
    private Button btnEvaluar;
    private TextView lblResult;
    private String resultado[];
    private TextView lblPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtExpresion = (EditText) findViewById(R.id.txtExpresion);
        this.btnEvaluar = (Button) findViewById(R.id.btnEvaluar);
        this.lblResult = (TextView) findViewById(R.id.lblResult);
        this.lblPost = (TextView) findViewById(R.id.lblPost);

        this.btnEvaluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String expresion = txtExpresion.getText().toString();
                Calculadora c = new Calculadora();
                resultado = c.solucionar(expresion);
                lblResult.setText(resultado[0]);
                lblPost.setText(resultado[1]);
            }
        });
    }


}
