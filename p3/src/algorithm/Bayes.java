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
    public String whichClassBelongTo(Matrix A){
        Matrix class1 = new Matrix(1,4), class2 = new Matrix(1,4);
        String rdo="Clasificación: ";

        class1=A.minus(averageClass1);
        class2=A.minus(averageClass2);


        double distanceClass1 = class1.distanceBayes();
        double distanceClass2 = class2.distanceBayes();


        if(distanceClass1<distanceClass2){
            rdo+=data.get(0).getName()+"\n";
        }else{
            rdo+=data.get(1).getName()+"\n";
        }

        rdo+="Distancia con clase 1: "+distanceClass1+"\n";
        rdo+="Distancia con clase 2: "+distanceClass2+"\n";
        rdo+="\nMedias\n";
        rdo+="Clase 1: ("+this.averageClass1.get(0,0)+", "+this.averageClass1.get(0,1)+", "+this.averageClass1.get(0,2)+", "+this.averageClass1.get(0,3)+")\n";
        rdo+="Clase 2: ("+this.averageClass2.get(0,0)+", "+this.averageClass2.get(0,1)+", "+this.averageClass2.get(0,2)+", "+this.averageClass2.get(0,3)+")\n";

        return rdo;
    }
}
