public class Test {
    public static void getInt(){
        System.out.println("Test.getInt");;
    }
    public void print() {
        getInt();
    }

}

class Test2 extends Test{
    public static void getInt(){
        System.out.println("Test2.getInt");;
    }

    public static void main(String[] args) {
        new Test2().print();
    }
}