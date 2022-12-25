package org.codeman.adapter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author hdgaadd
 * created on 2022/05/04
 *
 * description: 船长[ˈkæptɪn]
 */
@AllArgsConstructor
@NoArgsConstructor
public final class Captain {

    private RowingBoat rowingBoat;

    public void row() {
        rowingBoat.row();
    }
}
