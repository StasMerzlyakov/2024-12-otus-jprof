package ru.otus.solid.exchange;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.solid.model.Banknote;

public class GreedyBanknoteExchangeAlgorithm implements BanknoteExchangeAlgorithm {

    private static Logger logger = LoggerFactory.getLogger(GreedyBanknoteExchangeAlgorithm.class);

    private static int minBanknoteNominal = Banknote.BANKNOTE_50.nominal();

    @Override
    public Map<Banknote, Integer> calculateExchange(Map<Banknote, Integer> actualBanknoteCounts, int sumDesired)
            throws BanknoteExchangeAlgorithmException {

        logger.atDebug()
                .setMessage("calculateExchange start - banknotes: %n {}, sumDesired {}")
                .addArgument(actualBanknoteCounts.entrySet().stream()
                        .map(entry -> String.format(
                                "banknote: nominal %d, value: %d %n",
                                entry.getKey().nominal(), entry.getValue()))
                        .reduce(",", String::concat));

        if (sumDesired <= minBanknoteNominal) {
            throw new BanknoteExchangeAlgorithmException(
                    String.format("desired sum too low, min sum %d", minBanknoteNominal));
        }

        SortedMap<Banknote, Integer> banknotesSortedMap =
                new TreeMap<>(Comparator.comparingInt(Banknote::nominal).reversed());
        banknotesSortedMap.putAll(actualBanknoteCounts);

        HashMap<Banknote, Integer> resultMap = new HashMap<>();

        int remaider = sumDesired;

        for (var banknoteEntry : banknotesSortedMap.entrySet()) {
            var banknoteCount = banknoteEntry.getValue();
            if (banknoteCount > 0) {
                var banknote = banknoteEntry.getKey();
                var nominal = banknote.nominal();
                var count = remaider / nominal;
                count = count <= banknoteCount ? count : banknoteCount;
                remaider = remaider - count * nominal;
                resultMap.put(banknote, count);
                if (remaider == 0) {
                    break;
                }
            }
        }

        if (remaider != 0) {
            throw new BanknoteExchangeAlgorithmException("desired amount cannot be issued");
        }
        return resultMap;
    }
}
