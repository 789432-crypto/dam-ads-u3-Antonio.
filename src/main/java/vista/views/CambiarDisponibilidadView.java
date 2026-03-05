package vista.views;

import servicio.ClubDeportivo;
import modelo.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CambiarDisponibilidadView extends GridPane {

    public CambiarDisponibilidadView(ClubDeportivo club) {

        setPadding(new Insets(12));
        setHgap(8);
        setVgap(8);

        ComboBox<Pista> combo = new ComboBox<>();
        CheckBox disponible = new CheckBox("Disponible");
        Button cambiar = new Button("Aplicar");

        // 🔹 Cargar pistas desde BD
        combo.getItems().addAll(club.getPistas());

        // 🔹 Mostrar solo el ID en el ComboBox
        combo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Pista item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdPista());
            }
        });

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pista item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdPista());
            }
        });

        addRow(0, new Label("Pista"), combo);
        addRow(1, new Label("Estado"), disponible);
        add(cambiar, 1, 2);

        cambiar.setOnAction(e -> {
            try {

                Pista seleccionada = combo.getValue();

                if (seleccionada == null) {
                    showError("Debe seleccionar una pista");
                    return;
                }

                club.cambiarDisponibilidadPista(
                        seleccionada.getIdPista(),
                        disponible.isSelected()
                );

                showInfo("Disponibilidad actualizada");

                // Refrescar lista
                combo.getItems().clear();
                combo.getItems().addAll(club.getPistas());

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