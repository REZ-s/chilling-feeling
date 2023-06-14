package com.joolove.core.utils.recommend;

import java.util.*;
import java.util.stream.Collectors;

public class AprioriAlgorithm {

    public List<Set<String>> findFrequentItemsets(List<Set<String>> transactions, double minSupport) {
        List<Set<String>> frequentItemsets = new ArrayList<>();

        // Generate frequent 1-itemsets
        Map<String, Integer> itemCounts = countItemOccurrences(transactions);
        Map<String, Integer> frequent1Itemsets = filterBySupport(itemCounts, minSupport);

        // Add frequent 1-itemsets to the result
        frequentItemsets.addAll(
                frequent1Itemsets.keySet().stream().map(
                        item -> new HashSet<>(Collections.singleton(item))).collect(Collectors.toList()));

        // Generate frequent k-itemsets (k > 1)
        Map<Set<String>, Integer> frequentKItemsets = new HashMap<>(frequent1Itemsets);

        int k = 2;
        while (!frequentKItemsets.isEmpty()) {
            Map<Set<String>, Integer> candidateItemsets = generateCandidateItemsets(frequentKItemsets.keySet(), k);

            // Count occurrences of candidate itemsets in transactions
            for (Set<String> transaction : transactions) {
                List<Set<String>> subsets = generateSubsets(transaction, k);
                for (Set<String> candidate : candidateItemsets.keySet()) {
                    if (transaction.containsAll(candidate)) {
                        candidateItemsets.put(candidate, candidateItemsets.get(candidate) + 1);
                    }
                }
            }

            // Filter candidate itemsets by support
            frequentKItemsets = filterBySupport(candidateItemsets, minSupport);

            // Add frequent k-itemsets to the result
            frequentItemsets.addAll(frequentKItemsets.keySet());

            k++;
        }

        return frequentItemsets;
    }

    private Map<String, Integer> countItemOccurrences(List<Set<String>> transactions) {
        Map<String, Integer> itemCounts = new HashMap<>();

        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            }
        }

        return itemCounts;
    }

    private Map<String, Integer> filterBySupport(Map<String, Integer> itemCounts, double minSupport) {
        Map<String, Integer> frequentItemsets = new HashMap<>();

        int transactionCount = itemCounts.size();
        int minSupportCount = (int) Math.ceil(minSupport * transactionCount);

        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            if (entry.getValue() >= minSupportCount) {
                frequentItemsets.put(entry.getKey(), entry.getValue());
            }
        }

        return frequentItemsets;
    }

    private Map<Set<String>, Integer> generateCandidateItemsets(Set<Set<String>> frequentItemsets, int k) {
        Map<Set<String>, Integer> candidateItemsets = new HashMap<>();

        for (Set<String> itemset1 : frequentItemsets) {
            for (Set<String> itemset2 : frequentItemsets) {
                if (itemset1.size() == k - 1 && itemset2.size() == k - 1 && !itemset1.equals(itemset2)) {
                    Set<String> candidate = new HashSet<>(itemset1);
                    candidate.addAll(itemset2);

                    if (candidate.size() == k) {
                        candidateItemsets.put(candidate, 0);
                    }
                }
            }
        }

        return candidateItemsets;
    }

    private List<Set<String>> generateSubsets(Set<String> itemset, int k) {
        List<Set<String>> subsets = new ArrayList<>();

        generateSubsetsHelper(itemset, new HashSet<>(), k, subsets);

        return subsets;
    }

    private void generateSubsetsHelper(Set<String> itemset, Set<String> currentSubset, int k, List<Set<String>> subsets) {
        if (currentSubset.size() == k) {
            subsets.add(new HashSet<>(currentSubset));
            return;
        }

        for (String item : itemset) {
            if (!currentSubset.contains(item)) {
                currentSubset.add(item);
                generateSubsetsHelper(itemset, currentSubset, k, subsets);
                currentSubset.remove(item);
            }
        }
    }

}
