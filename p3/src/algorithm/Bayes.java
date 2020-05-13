package algorithm;

import java.util.List;
import utils.SimpleMatrix;

public class Bayes {
    private List<List<String>> data;
    private SimpleMatrix averageClass1;
    private SimpleMatrix averageClass2;
    private String class1;
    private String class2;

    public Bayes(List<List<String>> data){
        this.data=data;
        this.averageClass1 = new SimpleMatrix(0.0,0.0,0.0,0.0);
        this.averageClass2=new SimpleMatrix(0.0,0.0,0.0,0.0);
        this.class2="";
        calculateAverages();
    }

    /*Clase para calcular las medias. */
    private void calculateAverages(){
        this.class1=data.get(0).get(4);

        int totalOfDataClass1=0, totalOfDataClass2=0;

        for (int i=0; i<data.size();i++){
            if(data.get(i).get(4).equalsIgnoreCase(this.class1)){
                averageClass1.sum(Double.parseDouble(data.get(i).get(0)),Double.parseDouble(data.get(i).get(1)),Double.parseDouble(data.get(i).get(2)),Double.parseDouble(data.get(i).get(3)));
                totalOfDataClass1++;
            }else{
                if(this.class2=="")
                    this.class2=data.get(i).get(4);

                averageClass2.sum(Double.parseDouble(data.get(i).get(0)),Double.parseDouble(data.get(i).get(1)),Double.parseDouble(data.get(i).get(2)),Double.parseDouble(data.get(i).get(3)));
                totalOfDataClass2++;
            }
        }
        averageClass1.divide(totalOfDataClass1);
        averageClass2.divide(totalOfDataClass2);
        //averageClass1.print();
        //averageClass2.print();
    }

    /*Para ver a que clase pertenece*/
    public void whichClassBelongTo(Double first, Double second, Double third, Double fourth){
        SimpleMatrix class1 = new SimpleMatrix(), class2 = new SimpleMatrix();

        class1.setFirst(first-this.averageClass1.getFirst());
        class1.setSecond(second-this.averageClass1.getSecond());
        class1.setThird(third-this.averageClass1.getThird());
        class1.setFourth(fourth-this.averageClass1.getFourth());

        class2.setFirst(first-this.averageClass2.getFirst());
        class2.setSecond(second-this.averageClass2.getSecond());
        class2.setThird(third-this.averageClass2.getThird());
        class2.setFourth(fourth-this.averageClass2.getFourth());

        double distanceClass1 = class1.getFirst()*class1.getFirst()+class1.getSecond()*class1.getSecond()+class1.getThird()*class1.getThird()+class1.getFourth()*class1.getFourth();
        double distanceClass2 = class2.getFirst()*class2.getFirst()+class2.getSecond()*class2.getSecond()+class2.getThird()*class2.getThird()+class2.getFourth()*class2.getFourth();

        if(distanceClass1<distanceClass2){
            System.out.print("La muestra pertenece a la clase 1 "+this.class1+"\n");
        }else{
            System.out.print("La muestra pertenece a la clase 2 "+this.class2+"\n");
        }

    }
}
