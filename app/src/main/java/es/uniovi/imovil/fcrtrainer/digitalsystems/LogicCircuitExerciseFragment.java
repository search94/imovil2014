package es.uniovi.imovil.fcrtrainer.digitalsystems;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Random;

import es.uniovi.imovil.fcrtrainer.BaseExerciseFragment;
import es.uniovi.imovil.fcrtrainer.KeyboardView;
import es.uniovi.imovil.fcrtrainer.R;

/**
 * Created by search on 10/04/2016.
 */
public class LogicCircuitExerciseFragment extends BaseExerciseFragment {

    public class CircuitAdapter extends BaseAdapter{

        Activity act;
        int resource,tvresource;
        String [] array;
        int selectedPosition;
        public CircuitAdapter(Activity ac, int res, int textViewR, String[] objects) {
            act=ac;
            resource=res;
            tvresource=textViewR;
            array=objects;
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public String getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = ((Activity)getContext()).getLayoutInflater();
                v = vi.inflate(resource, null);
                RadioButton r = (RadioButton)v.findViewById(R.id.radioButtonC);
            }
            TextView tv = (TextView)v.findViewById(tvresource);
            tv.setText(array[position]);
            RadioButton r = (RadioButton)v.findViewById(R.id.radioButtonC);
            r.setChecked(position == selectedPosition);
            r.setTag(position);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = (Integer)view.getTag();
                    image=array[selectedPosition];
                    notifyDataSetChanged();
                }
            });
            return v;
        }



    }
    static final String currentCircuit="CURRENT_CIRCUIT";
    static final String currentEntries="CURRENT_ENTRIES";

    static final String title="Dadas las entradas a la izquierda y el circuito lógico calcula el resultado binario de la salida";

    private Random mRandom = new Random();

    private final int Default=2;
    private final int Easy=3;
    private final int Medium=4;
    private final int Hard=5;

    private TextView mClock;

    private Button testButton;
    private EditText resultText;
    private ImageView imageCircuit;
    private ListView circuits;
    private View mRootView;
    private String image;
    public TextView tv1,tv2,tv3,tv4,tv5;

    String [] array;
    int [] entries;
    @Override
    protected int obtainExerciseId() {
        return R.string.logic_circuit;
    }

    public static LogicCircuitExerciseFragment newInstance(){
        return new LogicCircuitExerciseFragment();
    }
    public LogicCircuitExerciseFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        mRootView = inflater.inflate(R.layout.fragment_logic_circuit, container,
                false);
        circuits=(ListView)mRootView.findViewById(R.id.listCircuits);
        testButton=(Button)mRootView.findViewById(R.id.TestButton);
        resultText=(EditText)mRootView.findViewById(R.id.text_view_answer);

        KeyboardView kv=(KeyboardView)mRootView.findViewById(R.id.keyboard);
        kv.assignEditText(resultText);

        resultText.setInputType(EditorInfo.TYPE_NULL);
        imageCircuit=(ImageView)mRootView.findViewById(R.id.imageCircuit);

        tv1=(TextView)mRootView.findViewById(R.id.entry1);tv1.setVisibility(View.VISIBLE);
        tv2=(TextView)mRootView.findViewById(R.id.entry2);tv2.setVisibility(View.VISIBLE);
        tv3=(TextView)mRootView.findViewById(R.id.entry3);tv3.setVisibility(View.INVISIBLE);
        tv4=(TextView)mRootView.findViewById(R.id.entry4);tv4.setVisibility(View.INVISIBLE);
        tv5=(TextView)mRootView.findViewById(R.id.entry5);tv5.setVisibility(View.INVISIBLE);

        imageCircuit.setImageResource(R.drawable.default_image);
        array=getResources().getStringArray(R.array.circuits);
        entries=new int[Default];
        CircuitAdapter ca=new CircuitAdapter(this.getActivity(),R.layout.list_circuit_item,R.id.listItemC,array);
        circuits.setAdapter(ca);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = resultText.getText().toString();
                isCorrect(test);
                System.out.println("Click");
                cargaPregunta();
            }
        });
        cargaPregunta();
        image=array[0];

        return mRootView;
    }

    public void generateRandomEntries(){
        for(int i=0;i<entries.length;i++) {
            entries[i] = mRandom.nextInt(2);
        }
    }

    @Override
    protected void startGame() {
        System.out.println("Image:" + image);
        int numb=searchNumber(image);
        changeEntries();
        commuteEntries(true);
        cargaPregunta();
        if(numb!=-1) {
            switch(numb){
                case 1:imageCircuit.setImageResource(R.drawable.c1);
                    break;
                case 2: imageCircuit.setImageResource(R.drawable.c2);
                    break;
                case 3: imageCircuit.setImageResource(R.drawable.c3);
                    break;
                default: imageCircuit.setImageResource(R.drawable.default_image);
            }

        super.startGame();
        }

    }
    public void cargaPregunta(){
        generateRandomEntries();
        switch (entries.length) {
            case 5:
                tv5.setText("" + entries[4]);
            case 4:
                tv4.setText(""+entries[3]);
            case 3:
                tv3.setText("" + entries[2]);
                break;
            default:
                tv1.setText(""+entries[0]);
                tv2.setText(""+entries[1]);


        }

    }
    @Override
    protected void cancelGame(){
        tv3.setVisibility(View.INVISIBLE);tv4.setVisibility(View.INVISIBLE);tv5.setVisibility(View.INVISIBLE);
        imageCircuit.setImageResource(R.drawable.default_image);
        entries=new int[Default];
        cargaPregunta();
        commuteEntries(false);
        super.cancelGame();

    }

    private int searchNumber(String cadena) {
        if(cadena!=null)
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            if (c >= '0' && c <= '9') {
                int x=Integer.parseInt(Character.toString(c));
                System.out.println("Número obtenido:"+x);
                return x;
            }
            }
        return -1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }
        else{
            image=savedInstanceState.getString(currentCircuit);
            entries=savedInstanceState.getIntArray(currentEntries);
        }

        if (mIsPlaying) {
            testButton.setVisibility(View.GONE);
        }
        //GUARDAR entries y numero de circuito
    }

    protected void isCorrect(String answer) {

        if(Integer.parseInt(answer)==calculate()){
            showAnimationAnswer(true);
            computeCorrectQuestion();
            cargaPregunta();

        }
        else {
            showAnimationAnswer(false);
            computeIncorrectQuestion();
        }
    }

    public int calculate(){
        boolean [] ent=new boolean[entries.length];
        for(int i=0;i<entries.length;i++){
            if(entries[i]==0) ent[i]=false;
            else ent[i]=true;
        }
        boolean result=calculate(ent);
        if(result)return 1;
        else return 0;
    }
    public boolean calculate(boolean [] entries){
        int dificult=entries.length;
        switch(entries.length){
            case Easy:
                return c1(entries);
            case Medium:
                return c2(entries);
            case Hard:
                return c3(entries);
            default:
                return cdefault(entries);
        }
    }

    @Override
    protected void endGame(){
        commuteEntries(false);
        imageCircuit.setImageResource(R.drawable.default_image);
        super.endGame();
    }
    public void changeEntries(){
        if(image.contains("Facil")){
            System.out.println(Easy);
            entries=new int[Easy];
        }
        else if(image.contains("Medio")){
            System.out.println(Medium);
            entries=new int[Medium];
        }
        else{
            System.out.println(Hard);
            entries=new int[Hard];
        }
    }
    public void commuteEntries(boolean activate){
        if(activate){
            switch (entries.length){
                case 5:
                    tv5.setVisibility(View.VISIBLE);
                case 4:
                    tv4.setVisibility(View.VISIBLE);
                case 3:
                    tv3.setVisibility(View.VISIBLE);


            }
        }
        else{
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.INVISIBLE);
            tv4.setVisibility(View.INVISIBLE);
            tv5.setVisibility(View.INVISIBLE);

            cargaPregunta();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(currentCircuit,image);
        outState.putIntArray(currentEntries,entries);
    }
    private boolean and(boolean b1,boolean b2){
        return b1&&b2;
    }
    private boolean or(boolean b1,boolean b2){
        return b1 || b2;
    }
    private boolean nor(boolean b1,boolean b2){
        return !or(b1,b2);
    }
    private boolean nand(boolean b1,boolean b2){
        return !and(b1, b2);
    }
    private boolean xor(boolean b1,boolean b2){
        return or(and(!b1,b2),and(b1,!b2));
    }

    //Circuito 1:
    public boolean c1(boolean [] b){return or(and(b[0],b[1]),!b[2]);}
    //Circuito 2:
    public boolean c2(boolean [] b){return nand(nor(b[0], b[1]), and(!b[2], b[3]));}
    //Circuito 3:
    public boolean c3(boolean [] b){
        boolean res1=and(b[0], b[1]);
        return nand(and(xor(res1,b[2]),!b[3]),or(res1, b[4]));
    }
    public boolean cdefault(boolean [] b){
        return !and(b[0],b[1]);
    }
}
