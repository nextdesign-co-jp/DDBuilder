package jp.co.nextdesign.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import jp.co.nextdesign.ddb.core.template.BdGeneratedFile;

/**
 * ファイルユーティリティ
 * @author murayama
 *
 */
public abstract class NdFileUtil {
	
	/**
	 * ファイル名前を拡張子部とそれ以外の文字列に分ける
	 * @param fileName
	 * @return
	 */
	public static String[] splitFileName(String fileName){
		String[] result = { "", "" };
		if (fileName != null && !fileName.isEmpty()){
			String[] splitted = fileName.split("\\.");
			if (splitted != null && splitted.length >= 2){
				for(int i=0; i<splitted.length - 1; i++){
					result[0] = splitted[i];
				}
				result[1] = splitted[splitted.length - 1];
			}
		}
		return result;
	}

	/**
	 * 拡張子を取得する
	 * @param f
	 * @return
	 */
	public static String getFileExtensionLowerCase(File f){
		String result = "";
		if (f != null && f.isFile()){
			String[] splitted = f.getName().split("\\.");
			if (splitted != null && splitted.length >= 2){
				result = splitted[splitted.length - 1].toLowerCase();
			}
		}
		return result;
	}
	
	/**
	 * 存在しなければディレクトリを作成する パスの中間にあるディレクトリも含めて、存在しなければ作成する
	 * DOSコマンドに実現できるオプションがない。またDOSコマンドを使うとLinuxでは使えない。
	 * @param dir
	 * @throws NdUtilException
	 */
	public static void makeDirectoryIfNotExists(File dir) throws NdUtilException {
		if (dir != null) {
			// File[] list = directory.listRoots();
			String[] splitted = dir.getAbsolutePath().split(Pattern.quote(File.separator));
			if (splitted != null && splitted.length > 0) {
				String checkName = splitted[0];
				for (int i = 1; i < splitted.length; i++) {
					checkName += File.separator + splitted[i];
					File checkDirectory = new File(checkName);
					if (!checkDirectory.exists()) {
						if (!checkDirectory.mkdir()) {
							throw new NdUtilException("作成できない： "
									+ checkDirectory.getAbsolutePath());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 指定されたディレクトリパスの上位から順に存在をチェックし存在しなければ作成する
	 * @param path
	 * @throws NdUtilException
	 */
	public static void makeDirectory(String path) throws NdUtilException {
		if (path != null) {
			String[] splitted = path.split(Pattern.quote(File.separator));
			if (splitted != null && splitted.length > 0) {
				String checkPath = "";
				for (int i = 0; i < splitted.length; i++) {
					checkPath += File.separator + splitted[i];
					File checkDir = new File(checkPath);
					if (!checkDir.exists()) {
						if (!checkDir.mkdir()) {
							throw new NdUtilException("作成できない： "
									+ checkDir.getAbsolutePath());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 存在しなければコピー元のファイルをコピー先にコピーして作成する
	 * DOSコマンドに実現できるオプションがない。またDOSコマンドを使うとLinuxでは使えない。
	 * @param from
	 * @param to
	 * @param characterCode
	 * @param replacer
	 * @throws NdUtilException
	 */
	public static void copyFileUsingReplacer(File from, File to, String characterCode, NdFileUtilStringReplacer replacer)
			throws NdUtilException {
		if (from != null && from.exists() && from.isFile() && to != null) {
			if (!to.exists()) {
				String fromFileName = from.getAbsolutePath();
				String toFileName = to.getAbsolutePath();
				BufferedReader br = null;
				BufferedWriter bw = null;
				try {
					// コピー先ファイル作成
					if (!to.createNewFile()) {
						throw new NdUtilException("NdFileUtil:" + "作成できない:" + to.getAbsolutePath());
					}
					// コピー元
					FileInputStream fis = new FileInputStream(fromFileName);
					br = new BufferedReader(new InputStreamReader(fis, characterCode));
					// コピー先
					FileOutputStream fos = new FileOutputStream(toFileName);
					bw = new BufferedWriter(new OutputStreamWriter(fos, characterCode));
					// コピー先に1行づつ追加
					String line;
					while ((line = br.readLine()) != null) {
						bw.write(replacer.replace(line));
						bw.newLine();
					}
					bw.flush();
				} catch (FileNotFoundException ex) {
					throw new NdUtilException("NdFileUtil:" + "ファイル名不明=" + fromFileName, ex);
				} catch (UnsupportedEncodingException ex) {
					throw new NdUtilException("NdFileUtil:" + "プロパティ（Application.properties）の文字コード名をチェック=" + characterCode, ex);
				} catch (IOException ex) {
					throw new NdUtilException("NdFileUtil:" + "ファイル行読込み例外=" + fromFileName, ex);
				} finally {
					try {
						if (br != null) {
							br.close();
						}
					} catch (Exception ex) {
						throw new NdUtilException("NdFileUtil:" + "ファイルクロース失敗：" + toFileName + "または" + fromFileName, ex);
					} finally {
						try {
							if (bw != null) {
								bw.close();
							}
						} catch (Exception ex) {
							throw new NdUtilException("NdFileUtil:" + "ファイルクロース失敗：" + toFileName + "または" + fromFileName, ex);
						}
					}
				}
			}
		}
	}
	
/**
 * 文字列行リストでファイルを新規作成or上書きする
 * @param from
 * @param to
 * @param characterCode
 * @throws NdUtilException
 */
	public static void writeFile(BdGeneratedFile from, File to, String characterCode)
			throws NdUtilException {
		if (from != null && to != null) {
			if (to.exists()){
				to.delete();
			}
			String toFileName = to.getAbsolutePath();
			BufferedWriter bw = null;
			try {
				// コピー先ファイル作成
				if (!to.createNewFile()) {
					throw new NdUtilException("NdFileUtil:" + "作成できない:" + to.getAbsolutePath());
				}
				// コピー先
				FileOutputStream fos = new FileOutputStream(toFileName);
				bw = new BufferedWriter(new OutputStreamWriter(fos, characterCode));
				// コピー先に1行づつ追加
				for(String line : from.getLineList()){
					bw.write(line);
					bw.newLine();
				}
				bw.flush();
			} catch (FileNotFoundException ex) {
				throw new NdUtilException("NdFileUtil:" + "ファイル名不明", ex);
			} catch (UnsupportedEncodingException ex) {
				throw new NdUtilException("NdFileUtil:" + "プロパティ（Application.properties）の文字コード名をチェック=" + characterCode, ex);
			} catch (IOException ex) {
				throw new NdUtilException("NdFileUtil:" + "ファイル行読込み例外", ex);
			} finally {
				try {
					if (bw != null) {
						bw.close();
					}
				} catch (Exception ex) {
					throw new NdUtilException("NdFileUtil:" + "ファイルクロース失敗：" + toFileName, ex);
				}
			}
		}
	}
}
