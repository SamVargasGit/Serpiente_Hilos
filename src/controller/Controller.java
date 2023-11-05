package controller;
import view.View;
public class Controller {
    private View view;
    public Controller(){
        this.view=new View();
    }
    public void run(){
        view.menu();
    }
    public static void main(String[] args) {
        new Controller().run();
    }
}
