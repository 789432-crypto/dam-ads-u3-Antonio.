package vista.views;

import servicio.ClubDeportivo;
import modelo.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class BajaSocioView extends GridPane {

    public BajaSocioView(ClubDeportivo club) {

        setPadding(new Insets(12));
        setHgap(8);
        setVgap(8);

        ComboBox<Socio> combo = new ComboBox<>();
        Button baja = new Button("Dar de baja");

        // 🔹 Cargar socios desde la BD
        combo.getItems().addAll(club.getSocios());

        // 🔹 Mostrar ID en el ComboBox
        combo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Socio item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdSocio());
            }
        });

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Socio item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdSocio());
            }
        });

        addRow(0, new Label("Socio"), combo);
        add(baja, 1, 1);

        baja.setOnAction(e -> {
            try {
                Socio seleccionado = combo.getValue();

                if (seleccionado == null) {
                    showError("Debe seleccionar un socio");
                    return;
                }

                club.eliminarSocio(seleccionado.getIdSocio());

                showInfo("Socio eliminado correctamente");

                // Actualizar lista
                combo.getItems().clear();
                combo.getItems().addAll(club.getSocios());

            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Error");
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}