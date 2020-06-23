# Dokumentácia
## 1. Aplikačná časť
#### + BE_Main
Trieda na vlastné testovanie.
#### + Classes
Trieda classes obsahuje metódu na vytvorenie kurzu. Metóda najskôr vyhľadáva učiteľa či existuje. Ak učiteľ existuje pridá do **tabuľky classes** kurz  s používateľom zadaným menom kurzu a učiteľom.
#### + Data
Trieda data, je pomocná trieda ktorá obsahuje statické premenné s údajmi ktoré je potrebné využívať opakovane, napríklad Scénu alebo typ používateľa
#### + DB_Connection
Táto trieda obsahuje metódu, pomocou ktorej sa pripájame na databázu postgreSQL.
#### + Display_content
Trieda display content momentálne používa konzolový výstup, na ktorý zobrazí obsah databázy students. Momentálne je táto trieda zatiaľ len v pracovnej verzii avšak pri nasledujúcom kontrolnom bode by mala už tieto údaje vypisovať priamo do grafického rozhrania.
#### + Users
Trieda User obsahuje tri hlavné metódy. Metóda new_user na základe používateľského vstupu vytvorí v tabuľke nového používateľa. Metóda chceck_user_password kontroluje na základe používateľského mena a typu používateľa a požívateľského hesla správnosť zadaného hesla. Z príslušnej tabuľky si na základe týchto údajov vyhľadá a skontroluje  daného používateľa. 
#### + Priklady
Trieda Priklady slúži na prácu s príkladmi. Cez túto klasu je možné príklady pridávať, zobrazovať alebo kontrolovať ich počet. Metóda **pocet_prikladov** sa používa pri pridávaní alebo aktualizovaní príkladu, kedy sa pomocou nej skontroluje či v databáze neexistuje rovnaký príklad. Na vyhľadanie príkladu slúži metóda **vyhladaj_priklad**, ktorá dokáže vyhľadať príklad bez nutnosti zadania celého jeho názvu, ignorujúc veľké a malé písmená. Ďalšou metódou je **pridaj_priklad**, ktorá používa transakciu - kontroluje sa či daný príklad podarilo úspešne pridať.
## 2. Grafické rozhranie
#### + Hlavna_stranka
Kód generujúci grafickú časť hlavnej stránky. Zatiaľ obsahuje len rozhranie prvého a druhého kontrolného bodu. V neskorších verziách plánujem hlavnú stránku diferencovať medzi rozhraním pre žiaka a rozhraním pre učiteľa, kde by žiakovi nebol prístupný všetok obsah ako učiteľovi, ako napríklad pridávanie kurzov alebo príkladov. 
#### + Hlavna_stranka_Controler
Kontroler hlavnej stránky. V tejto verzii obsahuje len načítavanie používateľom zadaných údajov z textových polí potrebné na vytváranie kurzu.
#### + Main
Hlavná trieda, po spustení aplikácie vytvorí a zobrazí používateľovi úvodnú prihlasovaciu stránku a priradí jej controller.
#### + Prihlásenie
Prihlásenie obsahuje obrazovku prihlasovacej stránky ktorá ho po úspešnom prihlásení presmeruje na hlavnú stránku. V prípade ak používateľ nie je registrovaný, obsahuje táto stránka takisto tlačidlo, ktoré ho presmeruje na stránku registrácie.
#### + Prihlásenie_Controler
Kontroler prihlasovania. Táto stránka načíta údaje zadané používateľom, a následne pomocou metód z aplikačnej vrstvy skontroluje v tabuľkách **students** a **teachers** či sa údaje zadané používateľom zhodujú s nejakým z už registrovaných používateľov.
#### + Registracia
Stránka registrácie obsahuje vstupy potrebné na registrovanie nového používateľa. V tejto verzii si nový používateľ iba zvolí aké chce mať poverenia resp. či chce byť žiakom alebo garantom, avšak v nasledujúcich verziách plánujeme takúto možnosť voľby odstrániť a namiesto toho pridať učiteľovi funkciu, ktorá mu umožní vymenovať žiaka za učiteľa.
#### + Registracia_Controler
Táto trieda obsahuje hlavne metódy, ktoré načítajú používateľský vstup ale takisto metódy z aplikačnej časti na kontrolu či sa používateľ nesnaží zadať údaje ktoré sa zhodujú s údajmi nejakého už registrovaného používateľa.
#### + Detail_popup_controler
Daná klasa slúži na zobrazenie konkrétneho príkladu - jeho typu, zadania, výsledku, počtu bodov, atď. Prostredníctvom nej sa taktiež dá príklad upraviť a uložiť do databázy. Oproti tabuľkovému zobrazeniu na hlavnej stránke sa tu nachádzajú všetky informácie o príklade.

## Databázová časť
#### 1. kontrolný bod
Pre prvý kontrolný bod sme sa rozhodli splniť pridávanie používateľov, teda pridávanie žiakov a učiteľov a takisto pridávanie kurzov, ktoré priamo referencujú na učiteľa. Pri tomto bode sme použili obyčajný insert do tabuliek pri čom sme využili prepared statementy, aby sme predišli útokom na databázu. Ako ďalší bezpečnostný prvok používame hashovanie hesiel. Pri registrácií nových používateľov sa ich heslá zahashujú pomocou MD5 hashovania a takto sú uložené v databáze. Na použité hashovanie sme využili návod zo stránky, ktorú uvádzame dole v referenciách. V prvom kontrolnom bode sme vytvorili tabuľky students, teachers a classes, z ktorých každú sme naplnili piatimi miliónmi dát. 

#### 2. kontrolný bod
V druhom kontrolnom bode sme realizovali scenáre **zobrazenia detailu konkrétneho záznamu** a scenár **úpravy záznamu** a takisto sme si pripravili podklad na ďalší scenár a to konkrétne vyhľadanie záznamu. 

#### 3. kontrolný bod
V treťom kontrolnom bode sme sa rozhodli realizovať scenáre **vymazávania záznamov** a **vyhľadávania záznamov** kde sme aplikovali aj filtrovanie podľa používateľom zadaných kritérií. Zároveň sme v tomto bode oddelili GUI pre žiaka a pre učiteľa, takže teraz sa každému zobrazia obrazovky, špeciálne pre nich.
Nižšie je uvedený konkrétny opis finálnej aplikácie a v nej použitých príkazov a stratégií so zameraním hlavne na databázové časti a prácu s ňou.

Tabuľka **problems**  obsahuje záznamy o príkladoch ktoré sú následne podávané žiakom na vypracovanie. Táto tabuľka priamo cez *FOREIGN KEY*  pracuje s tabuľkou **problems_students** a takisto s číselníkom *problem_types*. Nad touto tabuľkou sa pracuje vo viacerých častiach programu a učiteľ je v nej schopný záznamy *vymazávať, vkladať a takisto aj upravovať*, Navyše aj učiteľ aj žiak sú schopní v tabuľke vyhľadávať záznamy. Záznamy sa vyhľadávajú pomocou príkazu **LOWER(problems.name) like ?** vďaka čomu je zabezpečené, že používateľ nemusí zadať ani presný ani kompletný názov a tabuľka mu vráti všetky výsledky zodpovedajúce jeho hľadaniu.
Pri vyhľadávaní príkladov je použitý **JOIN na tabuľky problems, problem_types, a problems_students**. Tabuľka, resp. číselník problem_types je join-novaná preto, aby bolo možné vo vyhľadávaní použiť filtrovanie podľa typu príkladu. Tabuľka problems_students je joinnovaná z dôvodu výpoštu štatistiky, konkrétne z dôvodu výpočtu percentuálnej úspešnosti žiakov, ktorí daný príklad počítali. Na tento výpočet využívame **agregačnú funkciu avg**  matematické operátory a takisto funkcie **GROUP BY a ORDER BY**
Používateľ si je takisto schopný pri vyhľadávaní stránkovať výsledky a to tak, že si sám zvolí koľko záznamov chce mať na jednej stránke.Za týmto účelom sme na koniec SQL príkazu pridali OFFSET a LIMIT a tým vytvorili stránkovanie. Ak používateľ klikne na nejaký záznam zobrazenej tabuľke výsledkov hľadania, otvorí sa mu *popup okno s detailom daného záznamu* kde, v prípade ak sa jedná o učiteľa, môže daný príklad *pozmeniť alebo rovno vymazať.* Podľa zadaných vyhľadávacích parametrov sa vytvára string (cez string builder), ktorý sa následne vykoná v databáze a vráti požadovaný výsledok.

Pri treťom scenári sme implemetovali aj zaznačovanie dochádzky žiaka. Tabuľka dochádzky v sebe (ako je možné vidieť nižšie) obsahuje cedzie kľúče odkazujúce na žiaka a kurz v čase, ktorého sa žiak zúčastnil. Na to, aby mohol učiteľ dochádzku zaznačiť potrebuje poznať meno žiaka, názov kurzu a deň počas ktorého daný kurz prebehol. Po (správnom) zadaní mena žiaka zadávajúcemu učiteľovi pomáha pri vyplňovaní názvu kurzu autocomplete metóda ukazujúca všetky kurzy, ktoré daný žiak navštevuje. Návrhy autocompletu sa prispôsobujú tomu čo učiteľ do poľa "Názov kurzu" vpisuje. Na zrealizovanie sme využili **JOIN na tabuľky attendance, class_in_time a classes**, z ktorých je tabuľka attendance naplnená 50 miliónmi dát a zvyšné piatimi miliónmi. 

Transakcia je využitá pri vymazávaní kurzov v čase a k nim prislúchajúcej dochádzky. 

#### Tabuľky:
Naplnená na 5 000 000 záznamov.
CREATE TABLE students (
ID SERIAL  NOT NULL,  
name VARCHAR(100) NOT NULL,
password VARCHAR(100) NOT NULL,  
email_address VARCHAR(256) NOT NULL,  
phone_number INTEGER,
PRIMARY KEY(ID),
UNIQUE(name)
);

Naplnená na 5 000 000 záznamov.

CREATE TABLE teachers ( 
ID  SERIAL NOT NULL,  
name VARCHAR(100) NOT NULL,  
password VARCHAR(100) NOT NULL,  
email_address VARCHAR(256) NOT NULL,  
phone_number INTEGER,  
PRIMARY KEY(ID),
UNIQUE(name)
);

Naplnená na 5 000 000 záznamov.

CREATE TABLE classes (  
ID  SERIAL NOT NULL,  
name VARCHAR(50) NOT NULL,  
teacher_id INTEGER,  
PRIMARY KEY(ID),  
FOREIGN KEY(teacher_id) REFERENCES teachers,
UNIQUE(name)
);

Naplnená na 5 000 000 záznamov.

CREATE TABLE class_in_time (
ID SERIAL NOT NULL,
class_id INTEGER NOT NULL ,
teacher_id INTEGER NOT NULL,
date date,
time VARCHAR(30),
room VARCHAR(50),
PRIMARY KEY (ID),
FOREIGN KEY (class_id) REFERENCES classes,
FOREIGN KEY (teacher_id) REFERENCES teachers);

Naplnená na 50 000 000 záznamov.

CREATE TABLE attendance (
ID SERIAL NOT NULL,
student_id INTEGER NOT NULL,
class_in_time_id INTEGER NOT NULL,
FOREIGN KEY (student_id) REFERENCES students,
FOREIGN KEY (class_in_time_id) REFERENCES class_in_time);

Naplnená na 5 000 000 záznamov.

CREATE TABLE problems (            
ID SERIAL  NOT NULL,            
type_id  SMALLINT NOT NULL,            
name VARCHAR(100) NOT NULL,            
content text NOT NULL,            
result real NOT NULL,            
max_points smallint,            
PRIMARY KEY(ID),            
FOREIGN KEY (type_id) REFERENCES problem_types,
UNIQUE(name)
);

Naplnená na 5 000 000 záznamov.

CREATE TABLE problems_students ( <br/>
ID SERIAL NOT NULL,<br/>
student_id integer NOT NULL,<br/>
problem_id integer NOT NULL,<br/>
points smallint NUT NULL,<br/>
PRIMARY KEY(ID),<br/>
FOREIGN KEY (student_id) REFERENCES students,<br/>
FOREIGN KEY (problem_id) REFERENCES problems,<br/>
UNIQUE(student_id, problem_id)<br/>
);


#### Číselníky:
CREATE TABLE problem_types (  
ID      smallserial  NOT NULL,  
name	  varchar(100) NOT NULL,  
PRIMARY KEY(ID),
UNIQUE(name)
);

Referencie:

1. Návod na hash hesiel:
https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/







