package com.example.demo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class FileDBHandler {

	@Value("${app.datasource.path:}")
	private String filePath;

	@Value("${app.datasource.filename:}")
	private Set<String> names;

	@PostConstruct
	public void setup() {
		for (String name : names) {
			File f = new File(filePath, name);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}

			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized int nextSeq(String fileName) {
		File f = new File(this.filePath, fileName + "_seq.db");
	
		FileReader fr = null;
		BufferedReader br = null;

		int seq = 0;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			String line = null;

			while ((line = br.readLine()) != null) {
				seq = Integer.parseInt(line);
				seq++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);

			bw.write(String.valueOf(seq));
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
		
		return seq;
	}
	
	public synchronized <T> int selectRowsCount(String fileName, Select<T> select) {
		File f = new File(this.filePath, fileName + ".db");
		FileReader fr = null;
		BufferedReader br = null;

		List<T> rows = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			String line = null;

			while ((line = br.readLine()) != null) {
				if (rows == null) {
					rows = new ArrayList<>();
				}
				T t = select.select(line);
				if ( t != null ) {
					rows.add(t);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		return rows == null ? 0 : rows.size();
	}
	
	public synchronized <T> List<T> selectRows(String fileName, Select<T> select) {
		File f = new File(this.filePath, fileName + ".db");
		FileReader fr = null;
		BufferedReader br = null;

		List<T> rows = new ArrayList<>();
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			String line = null;

			while ((line = br.readLine()) != null) {
				T t = select.select(line);
				if ( t != null ) {
					rows.add(t);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		return rows;
	}
	
	public synchronized <T> List<T> selectRows(String fileName, Select<T> select, Comparator<T> sort, long skip, long limit) {
		List<T> rows = this.selectRows(fileName, select);
		if (rows == null || rows.size() < skip) {
			return null;
		}
		
		return rows.stream().sorted(sort).skip(skip).limit(limit).toList();
	}

	public synchronized <T> T selectRow(String fileName, Select<T> select) {
		File f = new File(this.filePath, fileName + ".db");
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			String line = null;

			while ((line = br.readLine()) != null) {
				T t = select.select(line);
				if (t != null) {
					return t;
				}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		return null;
	}

	public synchronized <T> int insert(String fileName, T object) {
		File f = new File(this.filePath, fileName + ".db");
		
		List<String> lines = this.selectRows(fileName, (line) -> line);
		
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
			
			if (lines != null && lines.size() > 0) {
				for (String line : lines) {
					bw.append(line);
					bw.newLine();
				}
			}
			
			bw.append(object.toString());
			if (!object.toString().endsWith("\n")) {
				bw.newLine();
			}
			bw.flush();

			return 1;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}

		return 0;
	}

	public synchronized <T> int update(String fileName, Update<T> update) {
		List<String> rows = this.selectRows(fileName, new Select<String>() {
			@Override
			public String select(String line) {
				return line;
			}
		});

		for (int i = 0; i < rows.size(); i++) {
			T t = update.update(rows.get(i));
			if (t != null) {
				rows.set(i, t.toString());
			}
		}
		
		File f = new File(this.filePath, fileName + ".db");
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);

			for (String row : rows) {
				bw.write(row);
				bw.newLine();
			}

			bw.flush();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}

		return 0;
	}
	
	public synchronized <T> int delete(String fileName, Predicate<String> deletePredicate) {
		List<String> rows = this.selectRows(fileName, new Select<String>() {
			@Override
			public String select(String line) {
				return line;
			}
		});

		rows = rows.stream().filter(line -> !deletePredicate.test(line)).toList();
		
		File f = new File(this.filePath, fileName + ".db");
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);

			for (String row : rows) {
				bw.write(row);
				bw.newLine();
			}

			bw.flush();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}

		return 0;
	}

	public static interface Select<T> {
		public T select(String line);
	}

	public static interface Update<T> {
		public T update(String line);
	}
}
