# DDBuilder & DDBuilderTemplate
ドメイン駆動設計のドメインモデルからJava Webアプリケーションを生成するツールです。

## Description
1.ドメイン駆動設計による反復型開発をサポーします。
2.ドメインモデルからJava Webアプリケーションを自動生成することで、DDDイテレーションを軽量化し、反復サイクルを短縮します。
3.ツールは、DDBuilderとDDBuilderTemplateで構成されています。
- DDBuilder(ツール本体)
https://github.com/nextdesign-co-jp/DDBuilder.git
- DDBuildertemplate(テンプレート)
https://github.com/nextdesign-co-jp/DDBuilderTemplate.git

## Requirement
1.Eclipse-jee。
2.Java5以降。

## Usage
1.DDBuilderとDDBuilderTemplateは、それぞれEclipseにインポートして使用します。
2.DDBuilderはJavaプロジェクトです。
3.DDBuilderTemplateはJavaEEプロジェクトです。
4.DDBuilderTemplateを改変した場合は、テンプレートをDDBuilder側に反映する必要があります。
反映する方法は、DDBuilderの「CopyTemplateIntoDDBuilder.bat」の内容を参考にしてください。

## Install

## Contribution

## Licence
[Apache License Version 2.0 January 2004]
http://www.apache.org/licenses/

## Author
Next Design Ltd.
http://www.nextdesign.co.jp/

# References
licensesフォルダを参照してください。

