package thread.Design.template;

/**
 * 模板设计模式
 */
public class Template {
    public final void print(String message){
        System.out.println("#######################");
        wraPrint(message);
        System.out.println("#######################");
    }

    protected void wraPrint(String message){}

    public static void main(String[] args) {
        Template t1 = new Template(){
            @Override
            protected void wraPrint(String message) {
                System.out.println("*" + message + "*");
            }
        };
        t1.print("t1 hello thread");

        Template t2 = new Template(){
            @Override
            protected void wraPrint(String message) {
                System.out.println("+" + message + "+");
            }
        };
        t2.print("t2 hello thread");

    }
}
