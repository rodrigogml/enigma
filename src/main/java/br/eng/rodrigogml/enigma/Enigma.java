package br.eng.rodrigogml.enigma;

import java.io.File;

import br.eng.rodrigogml.rfw.base.exceptions.RFWException;
import br.eng.rodrigogml.rfw.base.utils.BUConsole;
import br.eng.rodrigogml.rfw.base.utils.BUEncrypter;
import br.eng.rodrigogml.rfw.base.utils.BUFile;
import br.eng.rodrigogml.rfw.base.utils.BUString;

/**
 * Description: Classe Enigma para criptografar e descriptografar mensagens em arquivos de texto.<br>
 *
 * @author Rodrigo GML
 * @since 10.0 (10 de jun de 2021)
 */
public class Enigma {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      throw new RuntimeException("Passe o nome do arquivo para parâmetro. Passe um arquivo .txt para codificar seu conteúdo, ou passe um arquivo .enig para decodificar seu conteudo.");
    }

    File msgFile = new File(args[0]);
    if (!msgFile.exists()) {
      throw new RuntimeException("O arquivo '" + args[0] + "' não existe!");
    }

    if (args[0].toLowerCase().endsWith(".enig")) {
      decryptFile(args[0]);
    } else {
      encryptFile(args[0]);
    }
  }

  private static void decryptFile(String fileName) throws RFWException {
    System.out.print("Lendo conteúdo do arquivo '" + fileName + "'...");
    String content = BUFile.readFileContentToString(fileName);
    System.out.println(" OK!");

    String key = null;
    do {
      key = BUConsole.askPassword("Informe a chave de criptografia do arquivo:");
      key = BUString.removeAccents(BUString.removeNonUTF8(key));
      if (key == null) {
        System.out.println("Abortando...");
        System.exit(0);
      }
      if (key.length() < 20 || !key.trim().equals(key)) {
        System.out.println("Chave deve ter no mínimo 20 caracteres e não começar nem terminar com espaços!");
      } else {
        break;
      }
    } while (true);

    String decContent = BUEncrypter.decryptDES(content, key);
    String decFileName = fileName.substring(0, fileName.length() - 5);
    BUFile.writeFileContent(decFileName, decContent, false);
    System.out.println("Arquivo Descriptografado com sucesso! " + decFileName);
  }

  private static void encryptFile(String fileName) throws RFWException {
    System.out.print("Lendo conteúdo do arquivo '" + fileName + "'...");
    String content = BUFile.readFileContentToString(fileName);
    System.out.println(" OK!");

    String key = null;
    do {
      key = BUConsole.askPassword("Informe a chave de criptografia do arquivo:");
      key = BUString.removeAccents(BUString.removeNonUTF8(key));
      if (key == null) {
        System.out.println("Abortando...");
        System.exit(0);
      }
      if (key.length() < 20 || !key.trim().equals(key)) {
        System.out.println("Chave deve ter no mínimo 20 caracteres e não começar nem terminar com espaços!");
      } else {
        break;
      }
    } while (true);

    String encContent = BUEncrypter.encryptDES(content, key);
    String encFileName = fileName + ".enig";
    BUFile.writeFileContent(encFileName, encContent, false);
    System.out.println("Arquivo Criptografado com sucesso! " + encFileName);
  }

}
