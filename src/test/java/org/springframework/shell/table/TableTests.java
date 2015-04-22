/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.springframework.shell.table;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests for Table.
 *
 * @author Eric Bottard
 */
public class TableTests {

    @Test
    public void newEmptyTableRendersNothing() {
        assertThat(new Table().render().toString(), equalTo(""));
    }

    @Test
    public void addingRowsOneByOne() {
        Table table = new Table();
        assertThat(table.rows(), equalTo(0));
        table.nextRow();
        assertThat(table.rows(), equalTo(1));
        table.nextRow();
        assertThat(table.rows(), equalTo(2));
    }

    @Test
    public void addingRowsCreatingIntermediateRows() {
        Table table = new Table();
        assertThat(table.rows(), equalTo(0));
        table.nextRow();
        assertThat(table.rows(), equalTo(1));
        table.row(3);
        assertThat(table.rows(), equalTo(4));
        table.row(3);
        assertThat(table.rows(), equalTo(4));
        table.row(2);
        assertThat(table.rows(), equalTo(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void rowRangeTest() {
        Table table = new Table();
        table.row(-2);
    }

    @Test
    public void rowAddingCellsOneByOne() {
        Table table = new Table();
        Table.Row row = table.nextRow();
        row.nextCell();
        assertThat(row.cols(), equalTo(1));
        assertThat(table.cols(), equalTo(1));

        row.nextCell();
        assertThat(row.cols(), equalTo(2));
        assertThat(table.cols(), equalTo(2));
    }

    @Test
    public void rowAddingCellsCreatingIntermediateCells() {
        Table table = new Table();
        Table.Row row = table.nextRow();
        row.nextCell();
        assertThat(row.cols(), equalTo(1));
        assertThat(table.cols(), equalTo(1));

        row.cell(3);
        assertThat(row.cols(), equalTo(4));
        assertThat(table.cols(), equalTo(4));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void cellRangeTest() {
        Table table = new Table();
        Table.Row row = table.nextRow();
        row.cell(-3);
    }

    @Test
    public void api() {
        Table table = new Table();
        table.nextRow().nextCell().set("Hello");
    }

}