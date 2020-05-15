package algorithm;

import java.util.List;
import utils.Matrix;

public class Bayes {
    private List<Matrix> data;
    private Matrix averageClass1;
    private Matrix averageClass2;

    public Bayes(List<Matrix> data){
        this.data=data;
        this.averageClass1 = new Matrix(1,4);
        this.averageClass2=new Matrix(1,4);
        calculateAverages();
    }

    /*Clase para calcular las medias. */
    private void calculateAverages(){

        averageClass1.plusArray(data.get(0).getData());
        averageClass1=averageClass1.divide(data.get(0).getNumbeOfRows());

        averageClass2.plusArray(data.get(1).getData());
        averageClass2=averageClass2.divide(data.get(1).getNumbeOfRows());

    }

    /*Para ver a que clase pertenece*/
    public void whichClassBelongTo(Matrix A){
        Matrix class1 = new Matrix(1,4), class2 = new Matrix(1,4);

        class1=A.minus(averageClass1);
        class2=A.minus(averageClass2);


        double distanceClass1 = class1.distanceBayes();
        double distanceClass2 = class2.distanceBayes();

        System.out.print("Mediante el algoritmo de Bayes se ha calculado que ");
        if(distanceClass1<distanceClass2){
            System.out.print("la muestra pertenece a la clase 1 "+data.get(0).getName()+"\n");
        }else{
            System.out.print("la muestra pertenece a la clase 2 "+data.get(1).getName()+"\n");
        }

    }
}
