# Inventory Management (Java Swing + SQLite)

A simple desktop Inventory Management System built with Java Swing and SQLite.  
Features:
- Product management: add, update, delete, view products.
- SQLite database (inventory.db) for storage.
- GUI with tables, forms, search/filter, and buttons for CRUD operations.
- Input validation for price and quantity.
- Low-stock report generation.
- MVC architecture (Model, View, Controller).
- MIT License.

## Requirements
- Java 11 or newer
- Maven

## Build and run

1. Clone the repo (or copy files).
2. Build with Maven:
   mvn clean package
3. Run the JAR (assembly produces jar-with-dependencies):
   java -jar target/inventory-management-1.0.0-jar-with-dependencies.jar

The app will create (or reuse) `inventory.db` in the working directory and initialize the `products` table.

## Project structure (important files)
- src/main/java/com/inventory/model/Product.java — Product model
- src/main/java/com/inventory/dao/ProductDAO.java — Data access for SQLite
- src/main/java/com/inventory/db/DBHelper.java — DB connection and initialization
- src/main/java/com/inventory/view/MainView.java — Main GUI window
- src/main/java/com/inventory/view/ProductForm.java — Form dialog for add/edit
- src/main/java/com/inventory/controller/ProductController.java — Controller
- src/main/java/com/inventory/Main.java — App entry point
- pom.xml — Maven build (includes sqlite-jdbc dependency)

## Usage notes
- Use the Add button to create a product. Name, price, and quantity are validated.
- Select a row and click Edit or Delete to modify or remove products.
- Search by name or category using the search box (partial matches).
- Low Stock Report shows items with quantity below a threshold.

## Development notes
- The database file `inventory.db` is created in the working directory.
- To reset data, delete `inventory.db`.

## License
This project is licensed under the MIT License — see the LICENSE file for details.