# DiaTech Solutions — Progetto TSW UNISA

E-commerce per dispositivi medici dedicati alla gestione del diabete (CGM, glucometri, strisce, accessori).

## Struttura del Progetto

```
DiaTech/
├── src/
│   └── it/unisa/diatech/
│       ├── model/       ← Bean di dominio (Prodotto, Utente, Ordine, ...)
│       ├── dao/         ← Data Access Object (JDBC + JNDI DataSource)
│       ├── control/     ← Servlet (Controller MVC)
│       │   └── admin/  ← Servlet pannello amministratore
│       └── util/        ← Classi di utilità (PasswordUtil, SessionUtil, AuthFilter)
│
└── WebContent/
    ├── WEB-INF/
    │   ├── view/        ← JSP protette (non accessibili direttamente da URL)
    │   │   ├── common/  ← header.jsp, navbar.jsp, footer.jsp
    │   │   └── admin/   ← Viste area admin
    │   ├── lib/         ← Librerie JAR (mysql-connector)
    │   └── web.xml
    ├── styles/          ← CSS esterni (main.css, responsive.css, admin.css)
    ├── scripts/         ← JavaScript esterni (validazione.js, carrello-ajax.js, live-search.js)
    └── images/          ← Immagini e loghi
        └── prodotti/
```

## Tecnologie

- Java Servlet / JSP (Jakarta EE 5.0)
- Apache Tomcat 11
- MySQL 8.0 con JNDI DataSource
- HTML5 / CSS3 / JavaScript ES6 (nessun framework esterno)

## Come Avviare

1. Importare il progetto in Eclipse come **Dynamic Web Project esistente**
2. Configurare il server Tomcat 11 in Eclipse
3. Avviare MySQL e caricare lo script `schema_diatech.sql`
4. Run As → Run on Server
