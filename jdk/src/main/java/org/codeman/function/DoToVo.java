package org.codeman.function;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * created on 2024/01/04
 */
public class DoToVo {
    public static void main(String[] args) {
        DO do0 = new DO(0);
        DO do1 = new DO(1);
        DO do2 = new DO(2);
        List<DO> doList = Arrays.asList(do0, do1, do2);
        System.out.println(doList);

        List<VO> voList = map(doList, VO::buildRoleVO);
        System.out.println(voList);
    }

    public static <R, T> List<T> map(final Collection<R> list, final Function<? super R, ? extends T> function) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .map(function)
                .collect(Collectors.toList());
    }

    private static class DO implements Serializable {

        private static final long serialVersionUID = 2783609252111382305L;

        private int id;

        public DO(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "DO{"
                    + "id='" + id + '\''
                    + '}';
        }
    }

    private static class VO implements Serializable {

        private static final long serialVersionUID = 2783609252111382305L;

        private int id;

        public VO(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static VO buildRoleVO(final DO dto) {
            return Optional.ofNullable(dto)
                    .map(item -> new VO(item.getId())).orElse(null);
        }

        @Override
        public String toString() {
            return "VO{"
                    + "id='" + id + '\''
                    + '}';
        }
    }
}
