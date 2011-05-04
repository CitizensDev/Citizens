// $Id$
/*
 * WorldEdit
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.fullwall.Citizens.Traders;

public class ItemType {
	public static boolean shouldNotStack(int id) {
		if (((id >= 256 && id <= 259) || id == 261 || (id >= 267 && id <= 279)
				|| (id >= 281 && id <= 286) || (id >= 290 && id <= 294) || id == 346)) {
			return true;
		} else if ((id >= 298 && id <= 317)) {
			return true;
		}
		return (id >= 325 && id <= 327) || id == 335;
	}

	public static boolean usesDamageValue(int id) {
		return id == 17 // logs
				|| id == 35 // wool
				|| id == 43 // double slab
				|| id == 44 // slab
				|| id == 351; // dye
	}
}
