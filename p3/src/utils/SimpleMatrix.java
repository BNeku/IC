package utils;

public class SimpleMatrix {
   private Double firstRow;
   private Double secondRow;
   private Double thirdRow;
   private Double fourthRow;

   public SimpleMatrix(){};

    public SimpleMatrix(Double first, Double second, Double third, Double fourth){
        this.firstRow=first;
        this.secondRow=second;
        this.thirdRow=third;
        this.fourthRow=fourth;
    }

    public void sum(Double x, Double y, Double w, Double z){
        this.firstRow+=x;
        this.secondRow+=y;
        this.thirdRow+=w;
        this.fourthRow+=z;
    }

    public void divide(int x){
        this.firstRow/=x;
        this.secondRow/=x;
        this.thirdRow/=x;
        this.fourthRow/=x;
    }



    public double getFirst(){
        return this.firstRow;
    }
    public double getSecond(){
        return this.secondRow;
    }
    public double getThird(){
        return this.thirdRow;
    }
    public double getFourth(){
        return this.fourthRow;
    }

    public void setFirst(Double x){
        this.firstRow=x;
    }
    public void setSecond(Double x){
        this.secondRow=x;
    }
    public void setThird(Double x){
        this.thirdRow=x;
    }
    public void setFourth(Double x){
        this.fourthRow=x;
    }

    public void print(){
        System.out.print(this.firstRow+", "+this.secondRow+", "+this.thirdRow+", "+this.fourthRow+"\n");
    }
}
