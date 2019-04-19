package reynosojesus.ittepic.edu.tpdm_u3_practica1_jesusreynoso;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText num,nombre,edad,puesto;
    Button insertar,borrar,consultar,actualizar,irempleados;
    ListView listaEmpleados;
    FirebaseFirestore servicioBD;
    String con;
    List<Empleado> datosEmpleado;
    List<String> ramas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num = findViewById(R.id.txtnumero);
        nombre = findViewById(R.id.txtnombre);
        edad = findViewById(R.id.txtedad);
        puesto = findViewById(R.id.txtpuesto);
        insertar = findViewById(R.id.insertarEmpleado);
        borrar = findViewById(R.id.eliminarEmpleado);
        consultar = findViewById(R.id.consultarEmpleado);
        actualizar = findViewById(R.id.Actualizarempleado);
        listaEmpleados = findViewById(R.id.listaEmpleados);
        irempleados = findViewById(R.id.irEmpleado);

        irempleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                finish();
            }
        });

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarEmpleado();
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarEmpleado();
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarTodos();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEmpleado();
            }
        });

    }

    public void insertarEmpleado(){
        Empleado alu = new Empleado(num.getText().toString(),nombre.getText().toString(),edad.getText().toString(),puesto.getText().toString());
        servicioBD.collection("empleados")
                .document(num.getText().toString())
                .set(alu)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Insertado",Toast.LENGTH_LONG).show();
                        num.setText("");
                        nombre.setText("");
                        edad.setText("");
                        puesto.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void actualizarEmpleado() {
        servicioBD.collection("empleados")
                .whereEqualTo("numempleado",num.getText().toString())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void consultarTodos(){
        servicioBD.collection("empleados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot registro : task.getResult()){
                                Map<String,Object> datos=registro.getData();
                                ramas.add(registro.getId());
                                Empleado empleado=new Empleado(datos.get("numempleado").toString(),datos.get("nombre").toString(),datos.get("edad").toString(),datos.get("puesto").toString());
                                datosEmpleado.add(empleado);
                            }
                            ponerloEnListView();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "NO DATOS", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(MainActivity.this,
                                ""+con, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ponerloEnListView(){
        if(datosEmpleado.size()==0){
            return;
        }
        String[] datos= new String[datosEmpleado.size()];
        for (int i=0;i<datos.length;i++){
            Empleado al= datosEmpleado.get(i);
            datos[i]= al.nombre+"\n"+al.puesto;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datos);
        listaEmpleados.setAdapter(adapter);
    }

    private void eliminarEmpleado(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        final EditText numEliminar = new EditText(this);
        numEliminar.setHint("NO DEBE QUEDAR VACIO");
        alerta.setTitle("ATENCION").setMessage("ESCRIBA EL NUMERO DE EMPLEADO:")
                .setView(numEliminar)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(numEliminar.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this, "EL ID ESTA VACIO",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        eliminarEmpleado2(numEliminar.getText().toString());
                    }
                })
                .setNegativeButton("Cancelar",null)
                .show();
    }

    private void eliminarEmpleado2(String idEliminar){
        servicioBD.collection("alumnos")
                .document(idEliminar)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,
                                "SE ELIMINO!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                "NO SE ENCONTRO COINCIDENCIA!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
