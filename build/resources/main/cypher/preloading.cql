CREATE CONSTRAINT state_code IF NOT EXISTS FOR (n:State) REQUIRE (n.code) IS UNIQUE;
CREATE CONSTRAINT county_fips IF NOT EXISTS FOR (n:County) REQUIRE (n.fips) IS UNIQUE;
CREATE INDEX city_name IF NOT EXISTS FOR (n:City) ON n.name;
CREATE INDEX code_identifier IF NOT EXISTS FOR (n:Code) ON (n.identifier);

CREATE CONSTRAINT marker_id_value IF NOT EXISTS FOR (n:Marker) REQUIRE (n.uuid, n.value) IS UNIQUE;
CREATE INDEX marker_id IF NOT EXISTS FOR (n:Marker) ON (n.uuid);

CREATE CONSTRAINT patient_id IF NOT EXISTS FOR (n:Patient) REQUIRE (n.uuid) IS UNIQUE;
CREATE CONSTRAINT payer_id IF NOT EXISTS FOR (n:Payer) REQUIRE (n.uuid) IS UNIQUE;
CREATE CONSTRAINT organization_id IF NOT EXISTS FOR (n:Organization) REQUIRE (n.uuid) IS UNIQUE;
CREATE CONSTRAINT provider_id IF NOT EXISTS FOR (n:Provider) REQUIRE (n.uuid) IS UNIQUE;
CREATE CONSTRAINT encounter_id IF NOT EXISTS FOR (n:Encounter) REQUIRE (n.uuid) IS UNIQUE;
