Step 1: Install Git
Download and install Git if you haven’t already:
👉 https://git-scm.com/downloads

Step 2: Clone the Project
1. Open **IntelliJ IDEA**
2. Click **“Get from Version Control”**
3. Paste this link: ****https://github.com/kristykreeme/OWSB---Java.git****

4. Click **Clone** and let IntelliJ load everything

🗂 Project Folder Structure

Here’s how we’ve organized the files:
src/ └── main/ 
      ├── Admin/ → for user registration 
      ├── SalesManager/ → for PR and sales logic 
      ├── PurchaseManager/ → for PO generation 
      ├── InventoryManager/ → for inventory updates 
      ├── FinanceManager/ → for PO approvals & payments 
      ├── models/ → shared classes (User, PR, PO, Item, etc.)
      ├── utils/ → helper files (e.g., file handling) 
      └── MainApp.java → the main entry point of the program


👥 Roles & Responsibilities

| Role             | Person     | Task Summary                        |
|------------------|------------|-------------------------------------|
| Admin            | [Name]     | Register users, manage accounts     |
| Sales Manager    | [Name]     | Add sales, raise PR                 |
| Purchase Manager | [Name]     | View PRs, create PO                 |
| Inventory Manager| [Name]     | Manage and update stock             |
| Finance Manager  | [Name]     | Approve PO and handle payments      |

Work only inside your folder, and don’t edit others’ code without checking first.

---

🔄 How to Push & Pull Code

```bash

🟢 Every time before you start working:

git pull origin main


🛑 Always pull before pushing. If you skip this, it could cause conflicts and overwrite someone else’s work.

git add .
git commit -m "Add [what u did]"
git push origin main




