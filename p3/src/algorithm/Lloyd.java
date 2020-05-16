package algorithm;

import java.util.List;
import utils.Matrix;

public class Lloyd {
    private double tolerance;
    private List<Matrix> data;
    private int maxIterations;
    private int actualIterations;
    static final double razonDeAprendizaje = 0.1;
    private Matrix oldCenters;//es la matriz de centros tal cual esta antes de hacer una iteracion
    private Matrix updateCenters;//es como queda la matriz de centros tras terminar una iteración

    public Lloyd(List<Matrix> data, double t, int kmax, Matrix c){
        this.data=data;
        this.tolerance=t;
        this.maxIterations=kmax;
        this.actualIterations=0;
        this.oldCenters=c;
        this.updateCenters = c;
        updateCenters();
    }

    //realiza las iteraciones necesarias eligiendo que centro actualizar
    private void updateCenters(){

        while(actualIterations<maxIterations && !keepUpdating()){
            oldCenters = updateCenters;
            for(int i = 0; i<data.get(0).getNumbeOfRows(); i++){
                double[][] dataClass1 = data.get(0).getData();
                whichCenterToUpdate(dataClass1[0]);
            }
            for(int j = 0; j<data.get(1).getNumbeOfRows(); j++){
                double[][] dataClass2= data.get(1).getData();
                whichCenterToUpdate(dataClass2[0]);
            }
            actualIterations++;
        }
    }

    private void whichCenterToUpdate(double[] x){
        double[][] centersValues = updateCenters.getData();
        double[] center1 = centersValues[0];
        double[] center2 = centersValues[1];

        //calcula las distancias
        double distanceCenter1=0;
        double distanceCenter2=0;
        for(int i=0; i<center1.length;i++){
            distanceCenter1+= Math.pow(2,x[i]-center1[i]);
            distanceCenter2+=Math.pow(2,x[i]-center2[i]);
        }

        //si la distancia del centro1 es más pequeña que la del centro2, actualizamos el centro 1
        if(distanceCenter1<distanceCenter2){
            updateCenterChoosed(0, x);
        }else{//en caso contrario, actualizamos el centro 2
            updateCenterChoosed(1, x);
        }

    }

    //actualiza el centro elegido
    private void updateCenterChoosed(int z, double[] x){
        double[][] centersValues = updateCenters.getData();
        double[] center = centersValues[z];

        double rdo = 0.0;

        for(int i=0; i<center.length;i++){

            rdo=center[i]+ razonDeAprendizaje*(x[i]-center[i]);
            //actualizamos el valor en la matriz de centros update
            updateCenters.set(z,i,rdo);
        }
    }

    //calcula la distancia Euclidea y devuelve si se debe seguir iterando o no
    private boolean keepUpdating(){
        double rdoCenter1=0.0, rdoCenter2=0.0;
        double[][] oldData = oldCenters.getData();
        double[][] updateData = updateCenters.getData();

        double[] oldCenter1 = oldData[0];
        double[] oldCenter2 = oldData[1];

        double[] updateCenter1 = updateData[0];
        double[] updateCenter2 = updateData[1];

        for(int i = 0; i<oldCenters.getNumbeOfRows(); i++){
            rdoCenter1 += Math.pow(2,oldCenter1[i]-updateCenter1[i]);
            rdoCenter2 += Math.pow(2, oldCenter2[i]-updateCenter2[i]);
        }
        rdoCenter1=Math.sqrt(rdoCenter1);
        rdoCenter2=Math.sqrt(rdoCenter2);

        if(rdoCenter1< tolerance && rdoCenter2<tolerance){
            return true;
        }else{
            return false;
        }
    }

    public String whichClassBelongTo(Matrix A){
        double[][] dataCenters=updateCenters.getData();
        double[] center1 = dataCenters[0];
        double[] center2 = dataCenters[1];
        double[][] x = A.getData();//muestra a comprobar
        String rdo="Clasificación: ";

        double rdoCenter1=0.0, rdoCenter2=0.0;

        for(int i = 0; i<A.getNumberOfColumns(); i++){
            rdoCenter1+= Math.pow(2, x[0][i]-center1[i]);
            rdoCenter2+=Math.pow(1,x[0][i]-center2[i]);
        }

        if(rdoCenter1<rdoCenter2){
            rdo+=data.get(0).getName()+"\n";
        }else{
            rdo+= data.get(1).getName()+"\n";
        }

        rdo+="Distancia con clase 1: "+rdoCenter1+"\n";
        rdo+="Distancia con clase 2: "+rdoCenter2+"\n";
        rdo+="\nCentros\n";
        rdo+="x1 ("+center1[0]+", "+center1[1]+", "+center1[2]+", "+center1[3]+")\n";
        rdo+="x2 ("+center2[0]+", "+center2[1]+", "+center2[2]+", "+center2[3]+")\n";

        return rdo;
    }



}
