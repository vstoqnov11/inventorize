package bg.softuni.Inventorize.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
public class PaginationUtils {

    public int clampPage(int requestPage, int totalPage) {

        if (requestPage < 1) {
            return 1;
        }
        if (requestPage > totalPage) {
            return totalPage;
        }
        return requestPage;
    }

    public List<Integer> pageSequence (int totalPages) {

        return IntStream.rangeClosed(1, totalPages).boxed().toList();
    }
}
