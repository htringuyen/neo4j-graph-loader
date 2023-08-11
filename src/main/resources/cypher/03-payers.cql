    WITH row
    MERGE (p:Payer {uuid:row.Id})
    ON CREATE
        SET p.name = row.NAME,
            p.ownership = row.OWNERSHIP,
            p.phone = row.PHONE,
            p.amountCovered = toFloat(row.AMOUNT_COVERED),
            p.amountUncovered = toFloat(row.AMOUNT_UNCOVERED),
            p.revenue = toFloat(row.REVENUE),
            p.coveredEncounters = toInteger(row.COVERED_ENCOUNTERS),
            p.uncoveredEncounters = toInteger(row.UNCOVERED_ENCOUNTERS),
            p.coveredMedications = toInteger(row.COVERED_MEDICATIONS),
            p.uncoveredMedications = toInteger(row.UNCOVERED_MEDICATIONS),
            p.coveredProcedures = toInteger(row.COVERED_PROCEDURES),
            p.uncoveredProcedures = toInteger(row.UNCOVERED_PROCEDURES),
            p.coveredImmunizations = toInteger(row.COVERED_IMMUNIZATIONS),
            p.uncoveredImmunizations = toInteger(row.UNCOVERED_IMMUNIZATIONS),
            p.uniqueCustomers = toInteger(row.UNIQUE_CUSTOMERS),
            p.qolsAverage = toFloat(row.QOLS_AVG),
            p.memberMonths = toInteger(row.MEMBER_MONTHS)
    
    WITH row, p
    CALL custom.snowj.synthea.createAddress(row.ADDRESS, row.CITY, row.STATE_HEADQUARTERED, row.ZIP, null, null)
    YIELD node AS addr
    WITH p, addr
    CREATE (p)-[:HAS_ADDRESS]->(addr)

