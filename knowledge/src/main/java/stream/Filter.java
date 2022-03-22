package stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hdgaadd
 * Created on 2022/03/21
 * @decription 保留需要的
 */
public class Filter {
    public static void main(String[] args) {
        ArrayList<Integer> need = new ArrayList<>(Arrays.asList(1));
        List<Integer> ret = new ArrayList<>(Arrays.asList(1, 2, 3));

        ret = ret.stream().filter(item -> need.contains(item)).collect(Collectors.toList());

        System.out.println(ret);
    }
}
