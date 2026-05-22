public class GestorBunkers {

    private static final int NUM_BUNKERS = 4;
    private static final double POS_REL = 0.72;

    private List<Bunker> bunkers;

    private int anchoPantalla;
    private int altoPantalla;

    public GestorBunkers(int anchoPantalla, int altoPantalla) {

        this.anchoPantalla = anchoPantalla;
        this.altoPantalla = altoPantalla;

        bunkers = new ArrayList<>();

        inicializarBunkers();
    }

    private void inicializarBunkers() {

        int bunkerAncho = 60;

        int y = (int)(altoPantalla * POS_REL);

        int zonaAncho = anchoPantalla / NUM_BUNKERS;

        for (int i = 0; i < NUM_BUNKERS; i++) {

            int centroZona = zonaAncho * i + zonaAncho / 2;

            int bx = centroZona - bunkerAncho / 2;

            bunkers.add(new Bunker(bx, y));
        }
    }
}
