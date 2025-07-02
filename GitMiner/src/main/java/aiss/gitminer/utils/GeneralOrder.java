package aiss.gitminer.utils;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface GeneralOrder{
    boolean ascending();
    String getOrderBy();

    default Pageable getPageable(Integer page, Integer size) {
        return ascending() ?
                PageRequest.of(page, size, Sort.by(getOrderBy()).ascending()) :
                PageRequest.of(page, size, Sort.by(getOrderBy()).descending());
    }
}
