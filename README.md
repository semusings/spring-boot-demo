---
marp: true
theme: your-theme
paginate: true
header: '`© Software Engineering Musings - subscribe us on <https://youtube.com/@semusings>'
---

## Problem Context

Traditional _role-based access control (RBAC)_ solutions become difficult to administer and scale !
Why is it so???

- Complexity:
    - _adding new roles requires updating the role-permission matrix_
- Inflexibility:
    - _difficult to grant temporary or contextual access without creating new roles_
- Scalability:
    - _managing roles and permissions across numerous services is cumbersome_

_**Role-permission matrix for microservice-based order management system**_

| Role               | Order Service          | Payment Service | Inventory Service  |
|--------------------|------------------------|-----------------|--------------------|
| System Admin       | All Actions            | All Actions     | All Actions        |
| Order Manager      | Create, Update, Cancel | -               | -                  |
| Inventory Manager  | -                      | -               | Update Stock       |
| Payment Processor  | -                      | Process, Refund | -                  |
| + Discount Manager | Apply Discounts        | Approve Refunds | Check Stock Levels |

<!--

Adding a new role, like "Discount Manager," means updating permissions everywhere. Each service needs manual adjustments, making it easy to miss things.

It's hard to give someone temporary access or change permissions on the fly. This can slow down tasks that need immediate adjustments. 

For example, if a Payment Processor needs temporary access to inventory data for a fraud investigation, a new role must be created, or permissions must be manually adjusted, both of which are cumbersome and error-prone.

As we add more services and users, managing who can do what gets complicated. This can slow down the system and make it harder to keep everything secure
-->

--- 

## OpenFGA:

- fine-grained control by allowing permissions to be defined specifically within each microservice

---

## OpenFGA : Spring Boot Demo
