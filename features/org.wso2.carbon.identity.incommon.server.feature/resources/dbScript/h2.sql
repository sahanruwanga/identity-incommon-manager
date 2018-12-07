CREATE TABLE IF NOT EXISTS IDN_INCOMMON_SCHEDULER(
      ID INTEGER NOT NULL AUTO_INCREMENT,
      START_DATE VARCHAR (30),
      PERIOD INTEGER (5),
      URL VARCHAR (70) NOT NULL,
      LAST_MODIFIED_DATE VARCHAR (30),
      REFRESH_ENABLE VARCHAR (5),
      TENANT_ID INTEGER,
      PRIMARY KEY (ID),
      CONSTRAINT SCHEDULER_CONSTRAINT UNIQUE (URL, TENANT_ID)
);

CREATE TABLE IF NOT EXISTS IDN_INCOMMON_SAML_ENTITY(
      ID INTEGER NOT NULL AUTO_INCREMENT,
      ENTITY_ID VARCHAR (200),
      ENTITY_TYPE VARCHAR (3),
      STATUS VARCHAR (7),
      TENANT_ID INTEGER ,
      PRIMARY KEY (ID),
      CONSTRAINT ENTITY_ID_CONSTRAINT UNIQUE (ENTITY_ID)
);