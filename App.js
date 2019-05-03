/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {PureComponent} from 'react';
import {Platform, NativeModules, StyleSheet, Text, TouchableOpacity, View} from 'react-native';

const FideliOS = NativeModules.CalanderManager
const FidelAndroid = NativeModules.Fidel

type Props = {};
export default class App extends PureComponent<Props> {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Welcome to React Native!</Text>
        <Text style={styles.instructions}>To get started, edit App.js</Text>
        <TouchableOpacity onPress={this.checkPlatform}>
          <Text>Touch Me</Text>
        </TouchableOpacity>
      </View>
    );
  }

  setMetaData = (userEmail) => {
    if (Platform.OS === "ios") {
      FideliOS.setMetaData(userEmail);
    } else {
      FidelAndroid.setMetaData(userEmail)
    }
  }

  checkPlatform = () => {
    if (Platform.OS === "ios") {
      this.presentFidelIOS()
    } else {
      this.presentFidelAndroid()
    }
  }

  presentFidelIOS = async () => {
    try {
      this.setMetaData("a@b.c");
      var present = await FideliOS.present();
      console.log("success: ", present);
      return present;
    }
    catch (e) {
      console.log("error: ", e)
    }
  }

  presentFidelAndroid = () => {
    this.setMetaData("a@b.c")
    FidelAndroid.present().then((result) => {
      console.log("Resolved/Rejected Promise result: ", result)
    })
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
