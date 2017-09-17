/*
 * Copyright 2017 the original author or authors.
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
 */

package org.springframework.shell.samples.standard;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderSpecification;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.SizeConstraints;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;

@ShellComponent
public class OrderCommands {

	private List<OrderLine> lines = new ArrayList<>();

	private int orderNb = 1;

	private static String[] formatPrice(Object p) {
		return new String[]{String.format("$%.2f", p)};
	}

	@ShellMethod("Reset the current order.")
	public String resetOrder() {
		lines.clear();
		return "The current order has been reset";
	}

	@ShellMethod("Add an item to the current order.")
	public String addItem(
			@ShellOption(help = "the item reference") String item,
			@ShellOption(help = "unit price") @Min(0) float price,
			@ShellOption(help = "the number of items to add", defaultValue = "1") @Range(min = 1, max = 15) int qty
	) {
		lines.add(new OrderLine(item, qty, price));
		return String.format("%d %s(s) @ $%.2f have been added", qty, item, price);
	}

	@ShellMethod("Submit the order for processing.")
	public List<?> submitOrder() {
		List<Object> result = new ArrayList<>();
		result.add(String.format("Order #%d has been submitted", orderNb++));
		TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
		modelBuilder.addRow()
				.addValue("Item Description")
				.addValue("Unit Price")
				.addValue("Quantity")
				.addValue("Total");
		double total = 0D;
		for (OrderLine line : lines) {
			modelBuilder.addRow()
					.addValue(line.item)
					.addValue(line.price)
					.addValue(line.qty)
					.addValue(line.qty * line.price);
			total += line.qty * line.price;
		}
		modelBuilder.addRow()
				.addValue("")
				.addValue("")
				.addValue("")
				.addValue(total);

		TableBuilder builder = new TableBuilder(modelBuilder.build());
		builder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);
		builder.addOutlineBorder(BorderStyle.fancy_double);
		builder.paintBorder(BorderStyle.fancy_light_double_dash, BorderSpecification.TOP)
				.fromRowColumn(lines.size() + 1,0)
				.toBottomRight();

		builder.on(CellMatchers.ofType(Number.class)).addAligner(SimpleHorizontalAligner.right);
		builder.on(CellMatchers.column(0)).addSizer((a, b, c) -> new SizeConstraints.Extent(15, 15));
		builder.on(CellMatchers.ofType(Double.class)).addFormatter(OrderCommands::formatPrice);

		result.add(builder.build());
		lines.clear();
		return result;
	}

	public Availability submitOrderAvailability() {
		return lines.isEmpty() ? Availability.unavailable("the current order contains no items") : Availability.available();
	}

	private boolean isPrice(int r, int c, TableModel m) {
		return r >= 1 && r <= lines.size() && (c == 1 || c == 3);
	}

	private class OrderLine {
		private String item;

		private int qty;

		private double price;

		private OrderLine(String item, int qty, double price) {
			this.item = item;
			this.qty = qty;
			this.price = price;
		}
	}

}
