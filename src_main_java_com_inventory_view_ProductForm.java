package com.inventory.view;

import com.inventory.model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A modal dialog for creating or editing a Product.
 */
public class ProductForm extends JDialog {
    private JTextField nameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextArea descriptionArea;
    private JButton saveBtn;
    private JButton cancelBtn;

    private Product product;
    private boolean saved = false;

    public ProductForm(Frame owner, String title) {
        super(owner, title, true);
        initComponents();
    }

    private void initComponents() {
        nameField = new JTextField(30);
        categoryField = new JTextField(20);
        priceField = new JTextField(10);
        quantityField = new JTextField(10);
        descriptionArea = new JTextArea(4, 30);
        descriptionArea.setLineWrap(true);

        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        int row = 0;
        addRow(form, c, row++, "Name:", nameField);
        addRow(form, c, row++, "Category:", categoryField);
        addRow(form, c, row++, "Price:", priceField);
        addRow(form, c, row++, "Quantity:", quantityField);

        c.gridx = 0; c.gridy = row; c.gridwidth = 1;
        form.add(new JLabel("Description:"), c);
        c.gridx = 1; c.gridy = row++; c.gridwidth = 2;
        form.add(new JScrollPane(descriptionArea), c);

        JPanel buttons = new JPanel();
        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saved = true;
                    product = buildProductFromFields();
                    setVisible(false);
                }
            }
        });

        cancelBtn.addActionListener(e -> {
            saved = false;
            setVisible(false);
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row, String label, Component field) {
        c.gridx = 0; c.gridy = row; c.gridwidth = 1;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.gridy = row; c.gridwidth = 2;
        panel.add(field, c);
    }

    private boolean validateInput() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Name is required.");
            return false;
        }
        String priceStr = priceField.getText().trim();
        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
                showError("Price must be non-negative.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Price must be a number.");
            return false;
        }

        String qtyStr = quantityField.getText().trim();
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty < 0) {
                showError("Quantity must be zero or positive.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Quantity must be an integer.");
            return false;
        }
        return true;
    }

    private Product buildProductFromFields() {
        Product p = product == null ? new Product() : product;
        p.setName(nameField.getText().trim());
        p.setCategory(categoryField.getText().trim());
        p.setPrice(Double.parseDouble(priceField.getText().trim()));
        p.setQuantity(Integer.parseInt(quantityField.getText().trim()));
        p.setDescription(descriptionArea.getText().trim());
        return p;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation error", JOptionPane.ERROR_MESSAGE);
    }

    public void setProduct(Product p) {
        this.product = p;
        if (p != null) {
            nameField.setText(p.getName());
            categoryField.setText(p.getCategory());
            priceField.setText(String.valueOf(p.getPrice()));
            quantityField.setText(String.valueOf(p.getQuantity()));
            descriptionArea.setText(p.getDescription());
        } else {
            nameField.setText("");
            categoryField.setText("");
            priceField.setText("");
            quantityField.setText("");
            descriptionArea.setText("");
        }
    }

    public Product getProduct() {
        return product;
    }

    public boolean isSaved() {
        return saved;
    }
}