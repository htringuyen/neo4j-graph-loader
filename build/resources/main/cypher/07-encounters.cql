    WITH row

    // merge encounter and set basic info
    MATCH (patn:Patient {uuid:row.PATIENT}),
        (orgz:Organization {uuid:row.ORGANIZATION}),
        (prov:Provider {uuid:row.PROVIDER})
    WITH row, patn, orgz, prov
    MERGE (enc:Encounter {uuid:row.Id})
        ON CREATE
        SET enc.baseEncounterCost = toFloat(row.BASE_ENCOUNTER_COST),
            enc.totalClaimCost = toFloat(row.TOTAL_CLAIM_COST),
            enc.PAYER_COVERAGE = toFloat(row.PAYER_COVERAGE)
    MERGE (patn)-[:HAS_ENCOUNTER]->(enc)
    MERGE (enc)-[:HAS_PROVIDER]->(prov)
    MERGE (enc)-[:HAS_ORGANIZATION]->(orgz)

    // relabel encounter node
    WITH row, enc
    CALL apoc.create.setLabels(enc, ["Encounter", toUpper(row.ENCOUNTERCLASS)]) YIELD node
    
    // set start and end encounter time
    WITH row, enc
    MERGE (startMk:Marker {uuid:row.PATIENT, value:datetime(row.START)})
    MERGE (endMk:Marker {uuid:row.PATIENT, value:datetime(row.STOP)})
    WITH row, enc, startMk, endMk
    MERGE (enc)-[:ENCOUNTER_STARTED]->(startMk)
    MERGE (enc)-[:ENCOUNTER_ENDED]->(endMk)

    // set codes related to encounter
    WITH row, enc
    MERGE (typeCode:Code:SNOMED_CT {identifier:row.CODE})
        ON CREATE
        SET typeCode.description = row.DESCRIPTION
    MERGE (enc)-[:OF_TYPE]->(typeCode)

    FOREACH(ignore IN CASE WHEN row.REASONCODE IS NOT NULL AND row.REASONCODE <> '' THEN [1] ELSE [] END |
        MERGE (reasonCode:Code:SNOMED_CT {identifier:row.REASONCODE})
            ON CREATE
            SET reasonCode.description = row.REASONDESCRIPTION
        MERGE (enc)-[:FOR_REASON]->(reasonCode)
    )

