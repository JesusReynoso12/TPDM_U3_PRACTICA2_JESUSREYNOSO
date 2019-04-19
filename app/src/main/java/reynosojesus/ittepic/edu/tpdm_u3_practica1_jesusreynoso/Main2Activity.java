package reynosojesus.ittepic.edu.tpdm_u3_practica1_jesusreynoso;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    EditText nummesa,lugar,cantidadpersonas,reservado;
    Button insertar,irempleado,borrar,consultar,actualizar;
    ListView listaMesas;
    FirebaseFirestore servicioBD;
    String con;
    RadioButton rvsi,rvno;
    List<mesa> datosMesas;
    List<String> ramas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        nummesa = findViewById(R.id.txtnumeroMesa);
        lugar = findViewById(R.id.txtLugar);
        cantidadpersonas = findViewById(R.id.txtPersonas);
        insertar = findViewById(R.id.insertarMesa);
        borrar = findViewById(R.id.eliminarMesa);
        consultar = findViewById(R.id.consultarMesa);
        actualizar = findViewById(R.id.ActualizarMesa);
        irempleado = findViewById(R.id.irEmpleado);
        listaMesas = findViewById(R.id.listaMesas);
        rvsi = findViewById(R.id.rvsi);
        rvno = findViewById(R.id.rvno);
        irempleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,MainActivity.class));
                finish();
            }
        });

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarMesas();
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarMesa();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarMesa();
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarTodos();
            }
        });


    }


    public void insertarMesas(){
        String reservado="";
        if(rvsi.isChecked()){
            reservado="si";
        }else if(rvno.isChecked()){
            reservado="no";
        }else{
            Toast.makeText(Main2Activity.this,"Selecciona una opcion",Toast.LENGTH_LONG).show();
        }
        mesa mesa = new mesa(nummesa.getText().toString(),lugar.getText().toString(),cantidadpersonas.getText().toString(),reservado);
        servicioBD.collection("mesas")
                .document(nummesa.getText().toString())
                .set(mesa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main2Activity.this,"Insertado",Toast.LENGTH_LONG).show();
                        nummesa.setText("");
                        lugar.setText("");
                        cantidadpersonas.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void actualizarMesa() {
        servicioBD.collection("mesa")
                .whereEqualTo("nummesa",nummesa.getText().toString())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        })


                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void consultarTodos(){
        servicioBD.collection("mesa")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot registro : task.getResult()){
                                Map<String,Object> datos=registro.getData();
                                ramas.add(registro.getId());
                                mesa mesa = new mesa(datos.get("nummesa").toString(),datos.get("lugar").toString(),datos.get("cantidadpersona").toString(),datos.get("reservado").toString());
                                datosMesas.add(mesa);
                            }
                            ponerloEnListView();
                        } else {
                            Toast.makeText(Main2Activity.this,
                                    "NO DATOS", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(Main2Activity.this,
                                ""+con, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ponerloEnListView(){
        if(datosMesas.size()==0){
            return;
        }
        String[] datos= new String[datosMesas.size()];
        for (int i=0;i<datos.length;i++){
            mesa mesa= datosMesas.get(i);
            datos[i]= mesa.nummesa+"\n"+mesa.lugar;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datos);
        listaMesas.setAdapter(adapter);
    }

    private void eliminarMesa(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        final EditText numEliminar = new EditText(this);
        numEliminar.setHint("NO DEBE QUEDAR VACIO");
        alerta.setTitle("ATENCION").setMessage("ESCRIBA EL NUMERO DE EMPLEADO:")
                .setView(numEliminar)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(numEliminar.getText().toString().isEmpty()){
                            Toast.makeText(Main2Activity.this, "EL ID ESTA VACIO",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        eliminarMesa2(numEliminar.getText().toString());
                    }
                })
                .setNegativeButton("Cancelar",null)
                .show();
    }

    private void eliminarMesa2(String idEliminar){
        servicioBD.collection("alumnos")
                .document(idEliminar)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Main2Activity.this,
                                "SE ELIMINO!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this,
                                "NO SE ENCONTRO COINCIDENCIA!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
