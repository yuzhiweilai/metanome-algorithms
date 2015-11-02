package de.hpi.mpss2015n.approxind.inclusiontester;

import de.hpi.mpss2015n.approxind.InclusionTester;
import de.hpi.mpss2015n.approxind.utils.ColumnStore;
import de.hpi.mpss2015n.approxind.utils.SimpleColumnCombination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public final class HashSetInclusionTester implements InclusionTester {

    private final Map<Integer, Map<SimpleColumnCombination, HashSet<List<Long>>>> sets;
    private Map<SimpleColumnCombination, HashSet<List<Long>>> currentTable;

    public HashSetInclusionTester() {
        this.sets = new HashMap<>();
    }

    @Override
    public int[] setColumnCombinations(List<SimpleColumnCombination> combinations) {
        int[] activeTables = combinations.stream().mapToInt(SimpleColumnCombination::getTable).distinct().sorted().toArray();
        sets.clear();
        for (int table : activeTables) {
            sets.put(table, new HashMap<>());
        }
        for (SimpleColumnCombination combination : combinations) {
            sets.get(combination.getTable()).put(combination, new HashSet<>());
        }
        return activeTables;
    }

    @Override
    public void insertRow(long[] values, int rowCount) {
        for (Map.Entry<SimpleColumnCombination, HashSet<List<Long>>> entry : currentTable.entrySet()) {
            SimpleColumnCombination combination = entry.getKey();
            List<Long> combinationValues = new ArrayList<>(combination.getColumns().length);
            boolean allNull = true;
            for (int c : combination.getColumns()) {
                long value = values[c];
                allNull &= value == ColumnStore.NULLHASH;
                combinationValues.add(value);
            }
            if (!allNull) {
                entry.getValue().add(combinationValues);
            }
        }

    }

    @Override
    public boolean isIncludedIn(SimpleColumnCombination a, SimpleColumnCombination b) {
        HashSet<List<Long>> setA = sets.get(a.getTable()).get(a);
        HashSet<List<Long>> setB = sets.get(b.getTable()).get(b);
        return setB.containsAll(setA);
    }

	@Override
	public void startInsertRow(int table) {
		currentTable=sets.get(table);
	}


}