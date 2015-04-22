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

import java.util.ArrayList;
import java.util.List;

/**
 * Main class used for formatting and displaying tabular data.
 *
 * @author Eric Bottard
 */
public class Table {

    private List<Row> rows = new ArrayList<Row>();

    private int cols;

    public CharSequence render() {
        return "";
    }

    public Row nextRow() {
        Row row = new Row();
        rows.add(row);
        return row;
    }

    public Row row(int num) {
        if (num < 0) {
            throw new IndexOutOfBoundsException("row number must be positive");
        }
        if (num < rows.size()) {
            return rows.get(num);
        } else {
            Row result = null;
            for (int i = rows.size(); i <= num; i++) {
                result = nextRow();
            }
            return result;
        }
    }

    public int rows() {
        return rows.size();
    }

    public int cols() {
        return cols;
    }

    public class Row {

        private List<Cell> cells = new ArrayList<Cell>();

        public Cell cell(int col) {
            if (col < 0) {
                throw new IndexOutOfBoundsException("column number must be positive");
            }
            if (col < cells.size()) {
                return cells.get(col);
            } else {
                Cell result = null;
                for (int i = cells.size(); i <= col; i++) {
                    result = nextCell();
                }
                return result;
            }

        }

        public Cell nextCell() {
            Cell cell = new Cell();
            cells.add(cell);
            Table.this.cols = Math.max(Table.this.cols, cells.size());
            return cell;
        }

        public int cols() {
            return cells.size();
        }

    }

    public class Cell {

        private Object contents;

        public void set(Object contents) {
            this.contents = contents;
        }
    }
}
