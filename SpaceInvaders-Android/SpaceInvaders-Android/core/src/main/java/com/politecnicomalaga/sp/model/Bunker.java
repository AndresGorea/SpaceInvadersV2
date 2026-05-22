public class Bunker {

    private int x;
    private int y;

    private boolean destruido;

    public Bunker(int x, int y) {
        this.x = x;
        this.y = y;
        this.destruido = false;
    }

    public boolean procesarImpacto(int px, int py,
                                   int pw, int ph,
                                   boolean esDisparoEnemigo) {

        if (destruido) {
            return false;
        }

        // aquí comprobación de colisión

        return false;
    }

    public void draw(Graphics g) {
        // dibujar bunker
    }

    public void reset() {
        destruido = false;
    }

    public boolean isDestruido() {
        return destruido;
    }
}
