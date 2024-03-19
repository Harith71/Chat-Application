public class FactoryCoffee {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Coffee order = new Sugar( new Cream( CoffeeFactory.getCoffee("lr")));

        double cost = order.makeCoffee();

        System.out.println("Total cost: $" + cost);

        Coffee order2 = new Sugar(order);

        double cost2 = order2.makeCoffee();

        System.out.println("Total cost: $" + cost2);

    }

}


interface Coffee{
    public double makeCoffee();
}

class DarkRoastCoffee implements Coffee{

    private double cost = 3.50;
    @Override
    public double makeCoffee() {
        // TODO Auto-generated method stub

        System.out.println("DarkRoast Coffee: $3.50");
        return cost;
    }

}

class LightRoastCoffee implements Coffee{

    private double cost = 3.50;
    @Override
    public double makeCoffee() {
        // TODO Auto-generated method stub

        System.out.println("LightRoast Coffee: $3.50");
        return cost;
    }

}

class ColdBrewCoffee implements Coffee{

    private double cost = 3.50;
    @Override
    public double makeCoffee() {
        // TODO Auto-generated method stub

        System.out.println("Cold Brew Coffee: $3.50");
        return cost;
    }

}


class CoffeeFactory {


    public static Coffee getCoffee(String type) {

        switch(type) {

            case "cb":
                return new ColdBrewCoffee();


            case "lr":
                return new LightRoastCoffee();


            case "dr":
                return new DarkRoastCoffee();


            default:
                System.out.println("unknown option: returning Light Roast");
                return new LightRoastCoffee();
        }
    }


}

abstract class CoffeeDecorator implements Coffee{

    protected Coffee specialCoffee;

    public CoffeeDecorator(Coffee specialCoffee) {
        this.specialCoffee = specialCoffee;
    }

    public double makeCoffee() {
        return specialCoffee.makeCoffee();
    }
}

class ExtraShot extends CoffeeDecorator{

    private double cost = 1.50;

    public ExtraShot(Coffee specialCoffee) {
        super(specialCoffee);
    }

    public double makeCoffee() {
        return specialCoffee.makeCoffee() + addShot();
    }

    private double addShot() {
        System.out.println(" + extra Shot: $1.50");
        return cost;
    }
}

class Cream extends CoffeeDecorator{

    private double cost = .50;

    public Cream(Coffee specialCoffee) {
        super(specialCoffee);
    }

    public double makeCoffee() {
        return specialCoffee.makeCoffee() + addCream();
    }

    private double addCream() {
        System.out.println(" + add cream: $.50");
        return cost;
    }
}

class Sugar extends CoffeeDecorator{

    private double cost = .50;

    public Sugar(Coffee specialCoffee) {
        super(specialCoffee);
    }

    public double makeCoffee() {
        return specialCoffee.makeCoffee() + addSugar();
    }

    private double addSugar() {
        System.out.println(" + add Sugar: $.50");
        return cost;
    }
}
