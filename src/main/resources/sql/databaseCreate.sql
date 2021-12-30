DROP TABLE IF EXISTS STANICA CASCADE;
DROP TABLE IF EXISTS ULOGA CASCADE;
DROP TABLE IF EXISTS KORISNIK CASCADE;
DROP TABLE IF EXISTS VOZNJA CASCADE;
DROP TABLE IF EXISTS MJERENJE CASCADE;
DROP TABLE IF EXISTS KARTA CASCADE;
DROP TABLE IF EXISTS KARTICA CASCADE;
DROP TABLE IF EXISTS AKTIVACIJA CASCADE;
DROP TABLE IF EXISTS ZAVOZNJU CASCADE;

CREATE TABLE STANICA
(
  oznakaStanica INT NOT NULL,
  nazivStanica VARCHAR(100) NOT NULL,
  PRIMARY KEY (oznakaStanica)
);

CREATE TABLE ULOGA
(
  oznakaUloga VARCHAR(1) NOT NULL,
  PRIMARY KEY (oznakaUloga),
  CHECK(oznakaUloga = 'K' or oznakaUloga = 'A')
);

CREATE TABLE KORISNIK
(
  ime VARCHAR(50) NOT NULL,
  prezime VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  lozinka VARCHAR(40) NOT NULL,
  oznakaUloga VARCHAR(1) NOT NULL,
  PRIMARY KEY (email),
  FOREIGN KEY (oznakaUloga) REFERENCES ULOGA(oznakaUloga),
  CHECK (email LIKE '%_@_%.%_'),
  CHECK (LENGTH(lozinka) >= 8)
);

CREATE TABLE VOZNJA
(
  oznakaVlak VARCHAR(30) NOT NULL,
  oznakaStanica INT NOT NULL,
  odredisteoznakaStanica INT NOT NULL,
  cijenaVoznja FLOAT NOT NULL,
  datVrijPolaska TIMESTAMP NOT NULL,
  datVrijDolaska TIMESTAMP NOT NULL,
  PRIMARY KEY (oznakaVlak, oznakaStanica),
  FOREIGN KEY (oznakaStanica) REFERENCES STANICA(oznakaStanica),
  FOREIGN KEY (odredisteoznakaStanica) REFERENCES STANICA(oznakaStanica),
  CHECK (cijenaVoznja >= 0)
);

CREATE TABLE MJERENJE
(
  rbrVagon INT NOT NULL,
  kgFront INT NOT NULL,
  kgBack INT NOT NULL,
  oznakaVlak VARCHAR(30) NOT NULL,
  oznakaStanica INT NOT NULL,
  datVrijMjerenja TIMESTAMP NOT NULL,
  PRIMARY KEY (rbrVagon, oznakaVlak, oznakaStanica),
  FOREIGN KEY (oznakaVlak, oznakaStanica) REFERENCES VOZNJA(oznakaVlak, oznakaStanica)
);

CREATE TABLE KARTA
(
  oznakaKarta INT NOT NULL,
  pozicija VARCHAR(1),
  rbrVagon INT,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY (oznakaKarta),
  FOREIGN KEY (email) REFERENCES KORISNIK(email) ON DELETE CASCADE,
  CHECK (pozicija = 'F' OR pozicija = 'B')
);

CREATE TABLE KARTICA
(
  brojKartice VARCHAR(16) NOT NULL,
  imePrezVlasnik VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY(email),
  FOREIGN KEY (email) REFERENCES KORISNIK(email) ON DELETE CASCADE,
  CHECK (LENGTH(brojKartice) = 16)
);

CREATE TABLE AKTIVACIJA
(
  hash VARCHAR(36) NOT NULL,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY(email),
  FOREIGN KEY (email) REFERENCES KORISNIK(email) ON DELETE CASCADE
);

CREATE TABLE zaVoznju
(
  oznakaKarta INT NOT NULL,
  oznakaVlak VARCHAR(30) NOT NULL,
  oznakaStanica INT NOT NULL,
  PRIMARY KEY (oznakaKarta, oznakaVlak, oznakaStanica),
  FOREIGN KEY (oznakaKarta) REFERENCES KARTA(oznakaKarta) ON DELETE CASCADE,
  FOREIGN KEY (oznakaVlak, oznakaStanica) REFERENCES VOZNJA(oznakaVlak, oznakaStanica)
);