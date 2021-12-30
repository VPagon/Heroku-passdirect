SET datestyle = 'German';

INSERT INTO ULOGA
VALUES
    ('K'),
    ('A');

INSERT INTO KORISNIK(ime, prezime, email, lozinka, oznakaUloga)
VALUES
    ('admin', 'admin', 'admin@admin.com', 'adminzaporka', 'A'),
    ('korisnik', 'korisnik', 'korisnik@korisnik.com', 'korisnikzaporka', 'K'),
    ('Vinko', 'Sabolčec', 'vinko.sabolcec@fer.hr', 'vs365pass','A'),
    ('Ana', 'Drmić', 'ana.drmic@fer.hr', 'kvizmaster21','K'),
    ('Luka', 'Šantek', 'luka.santek@fer.hr', 'projektPROGI2021','K'),
    ('Vilim', 'Pagon', 'vilim.pagon@fer.hr', 'mojaLozinkaVP','K'),
    ('Lorena', 'Lazar', 'lorena.lazar@fer.hr', 'passdirectLL21','K'),
    ('Borna', 'Stanković', 'borna.stankovic@fer.hr', '$stankovic$','K'),
    ('Jakov', 'Juvančić', 'jakov.juvancic@fer.hr', 'JJferPass', 'K');

INSERT INTO STANICA(oznakaStanica, nazivStanica)
VALUES
    (0, ''),
    (1, 'Zagreb'),
    (2, 'Zadar'),
    (3, 'Split');

INSERT INTO VOZNJA(cijenaVoznja, datVrijPolaska, datVrijDolaska, oznakaStanica, odredisteOznakaStanica, oznakaVlak)
VALUES
    (0,      '29.12.2021. 15:30', '29.12.2021. 16:30', 0, 1, '128'),
    (55,     '29.12.2021. 16:30', '29.12.2021. 19:30', 1, 2, '128'),
    (45,     '29.12.2021. 19:35', '29.12.2021. 22:00', 2, 3, '128'),

    (0,      '23.12.2021. 15:30', '23.12.2021. 16:30', 0, 1, '311'),
    (51.5,   '23.12.2021. 16:30', '23.12.2021. 19:30', 1, 2, '311'),
    (71,     '23.12.2021. 19:45', '23.12.2021. 22:15', 2, 3, '311'),
    

    (0,      '23.12.2021. 20:10', '23.12.2021. 20:15', 0, 1, '151'),
    (77.23,  '23.12.2021. 20:20', '23.12.2021. 20:27', 1, 2, '151'),
    (87.55,  '23.12.2021. 20:45', '23.12.2021. 21:00', 2, 3, '151'),
    

    (0,      '24.12.2021. 15:30', '24.12.2021. 16:25', 0, 1, '425'),
    (57,     '24.12.2021. 16:25', '24.12.2021. 19:28', 1, 2, '425'),
    (77,     '24.12.2021. 19:35', '24.12.2021. 22:10', 2, 3, '425');


