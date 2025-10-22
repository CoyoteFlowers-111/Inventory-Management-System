package com.inventory.view;

import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Main application UI. Exposes components for controller wiring.
 */
public class MainView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn, reportBtn;
    private JTextField searchField;
    private JButton searchBtn;

    public MainView() {
        super("Inventory Management");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLayout(new BorderLayout());

        // Top panel: search and buttons
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(30);
        searchBtn = new JButton("Search");
        refreshBtn = new JButton("Refresh");
        top.add(new JLabel("Search:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(refreshBtn);

        add(top, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Name", "Category", "Price", "Quantity", "Description"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom panel: CRUD
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        deleteBtn = new JButton("Delete");
        reportBtn = new JButton("Low Stock Report");

        bottom.add(addBtn);
        bottom.add(editBtn);
        bottom.add(deleteBtn);
        bottom.add(reportBtn);

        add(bottom, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    public void setTableData(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity(),
                    p.getDescription()
            });
        }
    }

    public Integer getSelectedProductId() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        Object val = tableModel.getValueAt(row, 0);
        if (val instanceof Integer) return (Integer) val;
        return Integer.parseInt(val.toString());
    }

    // Expose UI components for controller actions
    public JButton getAddBtn() { return addBtn; }
    public JButton getEditBtn() { return editBtn; }
    public JButton getDeleteBtn() { return deleteBtn; }
    public JButton getRefreshBtn() { return refreshBtn; }
    public JButton getReportBtn() { return reportBtn; }
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchBtn() { return searchBtn; }

    // Utility methods
    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}