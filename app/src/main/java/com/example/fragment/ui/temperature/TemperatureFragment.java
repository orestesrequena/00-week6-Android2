package com.example.fragment.ui.temperature;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.fragment.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemperatureFragment extends Fragment {
    private EditText editTextCelsius, editTextFahrenheit;
    private Button buttonSave;
    private ListView listViewTemperature;
    private List<String> temperatureList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public TemperatureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main,menu);
    }

    //form de menu superior
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            //diferents facons de effacer le text
            //editTextFahrenheit.setText(null);
            // editTextFahrenheit.getText().clear();
            editTextCelsius.setText("");
            editTextFahrenheit.setText("");
            temperatureList.clear();
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);

        editTextCelsius = view.findViewById(R.id.editTextCelsius);
        editTextFahrenheit = view.findViewById(R.id.editTextFahrenheit);
        buttonSave = view.findViewById(R.id.buttonSave);
        listViewTemperature = view.findViewById(R.id.listViewTemperature);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editTextCelsius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String valeur = editable.toString();
                if (editTextCelsius.hasFocus() && !valeur.isEmpty() && isNumeric(valeur)) {
                    double valDouble = Double.valueOf(valeur);
                    String valeurInFahrenheit = TemperatureConverter.fahrenheitFromCelcius(valDouble);
                    editTextFahrenheit.setText(valeurInFahrenheit);
                }
            }
        });

        editTextFahrenheit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String valeur = s.toString();
                if (editTextFahrenheit.hasFocus() && !valeur.isEmpty() && isNumeric(valeur)) {
                    double valDouble = Double.valueOf(valeur);
                    String valeurInCelsius = TemperatureConverter.celsiusFromFahrenheit(valDouble);
                    editTextCelsius.setText(valeurInCelsius);
                }
            }
        });

        //sauvegarde des infos dans une listViewTemperature a partir du bouton
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String celsius = editTextCelsius.getText().toString();
                String fahrenheit = editTextFahrenheit.getText().toString();
                //on ajoute les infos dans un array
                temperatureList.add(String.format(getString(R.string.temperature_convert_text),celsius,fahrenheit));
                adapter.notifyDataSetChanged();// rafrachir les informations de la listeViewTemperature
            }
        });

        //gestion ListView
        //hay que poner getContext, porque estamos en un fragment, no en un activit, despues se pone el id del xml de la lista y despues se pone la informacion, aqui el array
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, temperatureList);
        listViewTemperature.setAdapter(adapter);
        //pour efface les elements de la liste et mettre a jour la liste dans la view
        listViewTemperature.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //effacer les elements dans la liste string temperatureList
                //rafrachir l'adapter
                temperatureList.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
