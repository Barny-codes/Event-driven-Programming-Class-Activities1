public class RefactoredMain {
    public static void main(String[] args) {
        DataModel model = new DataModel();
        model.addObserver(new LabelDisplay());
        model.setValue(10);
    }
}
