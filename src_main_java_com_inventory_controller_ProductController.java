package com.inventory.controller;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import com.inventory.view.MainView;
import com.inventory.view.ProductForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller connecting DAO and View.
 */
public class ProductController {
    private ProductDAO dao;
    private MainView view;

    public ProductController(ProductDAO dao, MainView view) {
        this.dao = dao;
        this.view = view;
        initController();
        loadProducts();
    }

    private void initController() {
        view.getAddBtn().addActionListener(e -> onAdd());
        view.getEditBtn().addActionListener(e -> onEdit());
        view.getDeleteBtn().addActionListener(e -> onDelete());
        view.getRefreshBtn().addActionListener(e -> loadProducts());
        view.getSearchBtn().addActionListener(e -> onSearch());
        view.getReportBtn().addActionListener(e -> onReport());
    }

    private void loadProducts() {
        List<Product> products = dao.getAllProducts();
        view.setTableData(products);
    }

    private void onAdd() {
        ProductForm form = new ProductForm(view, "Add Product");
        form.setProduct(null);
        form.setVisible(true);
        if (form.isSaved()) {
            Product p = form.getProduct();
            int id = dao.addProduct(p);
            if (id > 0) {
                view.showInfo("Product added (id=" + id + ").");
                loadProducts();
            } else {
                view.showError("Failed to add product.");
            }
        }
    }

    private void onEdit() {
        Integer id = view.getSelectedProductId();
        if (id == null) {
            view.showError("Select a product to edit.");
            return;
        }
        // Fetch current product details from list (we could load by id; for simplicity reuse loaded list)
        // We'll search through DAO results
        Product target = null;
        for (Product p : dao.getAllProducts()) {
            if (p.getId().equals(id)) {
                target = p; break;
            }
        }
        if (target == null) {
            view.showError("Product not found.");
            return;
        }
        ProductForm form = new ProductForm(view, "Edit Product");
        form.setProduct(target);
        form.setVisible(true);
        if (form.isSaved()) {
            Product updated = form.getProduct();
            updated.setId(target.getId());
            boolean ok = dao.updateProduct(updated);
            if (ok) {
                view.showInfo("Product updated.");
                loadProducts();
            } else {
                view.showError("Failed to update product.");
            }
        }
    }

    private void onDelete() {
        Integer id = view.getSelectedProductId();
        if (id == null) {
            view.showError("Select a product to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Delete product ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = dao.deleteProduct(id);
            if (ok) {
                view.showInfo("Product deleted.");
                loadProducts();
            } else {
                view.showError("Failed to delete product.");
            }
        }
    }

    private void onSearch() {
        String q = view.getSearchField().getText().trim();
        if (q.isEmpty()) {
            loadProducts();
            return;
        }
        List<Product> results = dao.searchProducts(q);
        view.setTableData(results);
    }

    private void onReport() {
        String input = JOptionPane.showInputDialog(view, "Show items with quantity <= (default 5):", "Low stock threshold", JOptionPane.PLAIN_MESSAGE);
        int threshold = 5;
        if (input != null && !input.trim().isEmpty()) {
            try {
                threshold = Integer.parseInt(input.trim());
                if (threshold < 0) threshold = 5;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid number. Using default 5.", "Warning", JOptionPane.WARNING_MESSAGE);
                threshold = 5;
            }
        } else if (input == null) {
            // user cancelled
            return;
        }
        List<Product> low = dao.getLowStock(threshold);
        if (low.isEmpty()) {
            view.showInfo("No low stock items (<= " + threshold + ").");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Low stock items (<= ").append(threshold).append("):\n\n");
        for (Product p : low) {
            sb.append(String.format("ID:%d  %s (%s) — Qty: %d  Price: %.2f\n",
                    p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getPrice()));
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new java.awt.Dimension(600, 300));
        JOptionPane.showMessageDialog(view, sp, "Low Stock Report", JOptionPane.INFORMATION_MESSAGE);
    }
}