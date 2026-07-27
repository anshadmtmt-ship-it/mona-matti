# MONA MATTI — Database Documentation

## 1. Relational Entity Schema

### Table: `product`
| Column Name | SQL Type | Constraints | Description |
|---|---|---|---|
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique product identifier |
| `name` | `VARCHAR(255)` | `NOT NULL` | Product title |
| `hero_title` | `VARCHAR(255)` | `NOT NULL` | Main hero section headline |
| `hero_subtitle` | `VARCHAR(500)` | `NULLABLE` | Secondary hero tagline |
| `price` | `DOUBLE` | `NOT NULL` | Unit price in USD |
| `description` | `TEXT` | `NULLABLE` | Full product specification |
| `main_image` | `VARCHAR(255)` | `NULLABLE` | Image resource path |
| `stock_status` | `VARCHAR(50)` | `NOT NULL` | Stock availability status |
| `created_at` | `DATETIME` | `NOT NULL` | Entity creation timestamp |
| `updated_at` | `DATETIME` | `NULLABLE` | Entity last update timestamp |

---

### Table: `reservation`
| Column Name | SQL Type | Constraints | Description |
|---|---|---|---|
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique reservation identifier |
| `full_name` | `VARCHAR(50)` | `NOT NULL` | Customer full name (3-50 chars) |
| `signature` | `TEXT` | `NOT NULL` | Canvas signature data URL (`image/png`) |
| `created_at` | `DATETIME` | `NOT NULL`, `INDEX(idx_reservation_created_at)` | Reservation timestamp |

---

## 2. Dynamic Identification Helper
The `Reservation` entity provides a formatted identification code method:
```java
public String getFormattedId() {
    return String.format("MM-%06d", id);
}
```
Example Output: `MM-000001`
