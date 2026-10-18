
package com.example.crudwithvaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final CustomerRepository repository;
    private final Grid<Customer> grid = new Grid<>(Customer.class);

    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");

    private Customer selectedCustomer;

    public MainView(CustomerRepository repository) {
        this.repository = repository;

        Button addButton = new Button("Add customer", event -> addCustomer());

        Button updateButton = new Button("Update customer", event -> updateCustomer());

        Button deleteButton = new Button("Delete customer", event -> deleteCustomer());

        grid.setColumns("firstName", "lastName");

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedCustomer = event.getValue();

            if (selectedCustomer != null) {
                firstName.setValue(selectedCustomer.getFirstName());
                lastName.setValue(selectedCustomer.getLastName());
            }
        });

        add(firstName, lastName, addButton, updateButton, deleteButton, grid);

        refreshGrid();
    }

    private void addCustomer() {
        if (!firstName.isEmpty() && !lastName.isEmpty()) {
            repository.save(new Customer(
                    firstName.getValue(),
                    lastName.getValue()
            ));

            clearFields();
            refreshGrid();
        }
    }

    private void updateCustomer() {
        if (selectedCustomer != null
                && !firstName.isEmpty()
                && !lastName.isEmpty()) {

            selectedCustomer.setFirstName(firstName.getValue());
            selectedCustomer.setLastName(lastName.getValue());

            repository.save(selectedCustomer);

            clearFields();
            refreshGrid();
        }
    }

    private void deleteCustomer() {
        if (selectedCustomer != null) {
            repository.delete(selectedCustomer);

            clearFields();
            refreshGrid();
        }
    }

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        selectedCustomer = null;
        grid.asSingleSelect().clear();
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }
}

