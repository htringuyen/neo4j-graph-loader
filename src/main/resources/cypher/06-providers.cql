    WITH row
    MATCH (orgz:Organization {uuid:row.ORGANIZATION})
    WITH row, orgz
    MERGE (prov:Provider {uuid:row.Id})
        ON CREATE
        SET prov.name = row.NAME,
            prov.gender = row.GENDER,
            prov.speciality = row.SPECIALITY,
            prov.numEncounters = toInteger(row.ENCOUNTERS),
            prov.numProcedures = toInteger(row.PROCEDURES)
    CREATE (prov)-[:BELONGS_TO]->(orgz)
    WITH row, prov
    CALL custom.snowj.synthea.createAddress(row.ADDRESS, row.CITY, row.STATE, row.ZIP, toFloat(row.LAT), toFloat(row.LON))
    YIELD node AS addr
    WITH prov, addr
    CREATE (prov)-[:HAS_ADDRESS]->(addr)

