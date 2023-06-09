package sklep.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class SearchFiltersUtil {

    public static PageRequest getPageRequest(String sortBy,
                                             String order,
                                             int page,
                                             int pageSize,
                                             List<String> allowedParams){
        Sort sort = SearchFiltersUtil.getSort(sortBy, order, allowedParams);
        return PageRequest.of(page, pageSize, sort);
    }

    public static Sort getSort(String sortBy,
                               String order,
                               List<String> allowedParams
    ){
        String sortedField = allowedParams.contains(sortBy)? sortBy: allowedParams.get(0);
        return Sort.by(
                order.equals("asc")? Sort.Direction.ASC: Sort.Direction.DESC,
                sortedField
        );
    }

}
