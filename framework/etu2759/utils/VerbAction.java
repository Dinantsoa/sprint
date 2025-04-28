package framework.etu2759.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import framework.etu2759.annotation.Param;
import framework.etu2759.erreur.AllErreur;
import framework.etu2759.erreur.Erreur;

import javax.servlet.ServletException;


public class VerbAction{
	private String verb;
	private Method action;
	public VerbAction()
	{
		
	}
	public VerbAction(String verb, Method action) {
		this.verb = verb;
		this.action = action;
	}
	public String getVerb() {
		return verb;
	}
	public Method getAction() {
		return action;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public void setAction(Method action) {
		this.action = action;
	}
	public Method getMethodByName(Class<?> cls, String methodName) throws Exception { 
		// Obtenir toutes les méthodes de la classe
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			// Vérifier si le nom de la méthode correspond au nom donné
			if (method.getName().equals(methodName)) {
				return method;
				
			}
		}
		Exception e=new Exception("Tsisy methode "+methodName);
		throw e;
		// Retourner null si aucune méthode correspondante n'est trouvée
		
	}
	public ArrayList<String> makaParametre(HttpServletRequest request) throws Exception
	{
		Enumeration<String> parameterNames = request.getParameterNames();
		ArrayList<String> valiny=new ArrayList<String>();

		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
				valiny.add(name);
			
			
		}
		if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
			// Récupérer les noms des parties (fichiers et autres parties multipart)
			for (Part part : request.getParts()) {
				String partName = part.getName();
				// Ajouter uniquement si ce nom n'est pas déjà dans la liste
				if (!valiny.contains(partName)) {
					valiny.add(partName);
				}
			}
		}
		return valiny;
	}
	public boolean isPrimitiveOrString(Class<?> paramType) {
		return paramType.isPrimitive() || paramType.equals(String.class);
	}
	private static Object mamadikaObject(Class<?> clazz, String value) throws Exception {
    if (value == null) {
        if (clazz == int.class) return 0;
        if (clazz == boolean.class) return false;
        if (clazz == double.class) return 0.0;
        if (clazz == long.class) return 0L;
        if (clazz == float.class) return 0f;
        if (clazz == short.class) return (short) 0;
        if (clazz == byte.class) return (byte) 0;
        return null; // Pour les objets
    }

    try {
        if (clazz == String.class) {
            return value;
        } else if (clazz == int.class || clazz == Integer.class) {
            return Integer.parseInt(value);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (clazz == double.class || clazz == Double.class) {
            return Double.parseDouble(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return Long.parseLong(value);
        } else if (clazz == float.class || clazz == Float.class) {
            return Float.parseFloat(value);
        } else if (clazz == short.class || clazz == Short.class) {
            return Short.parseShort(value);
        } else if (clazz == byte.class || clazz == Byte.class) {
            return Byte.parseByte(value);
        } else if (clazz == java.util.Date.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(value);
        } else if (clazz == java.sql.Date.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = formatter.parse(value);
            return new java.sql.Date(utilDate.getTime());
        } else if (clazz == Timestamp.class) {
            if (value.contains("T")) {
                LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return Timestamp.valueOf(localDateTime);
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date utilDate = formatter.parse(value);
                return new Timestamp(utilDate.getTime());
            }
        } else if (clazz == LocalDateTime.class) {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    } catch (Exception e) {
        // Vérifie si la classe a un constructeur sans argument avant d’essayer de l'instancier
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException("Impossible de convertir `" + value + "` en `" + clazz.getName() + "`", ex);
        }
    }
    
    throw new IllegalArgumentException("Cannot convert String to " + clazz.getName());
}

	public static Field getField(Object object, String fieldName) throws NoSuchFieldException {
		if (object == null || fieldName == null || fieldName.isEmpty()) {
			throw new IllegalArgumentException("L'objet ou le nom du champ ne peut pas être null ou vide.");
		}
		
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				// Tente de récupérer le champ dans la classe actuelle
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true); // Rend le champ accessible même s'il est privé
				return field;
			} catch (NoSuchFieldException e) {
				// Si le champ n'existe pas dans la classe actuelle, on regarde dans la classe parente
				clazz = clazz.getSuperclass();
			}
		}
		
		// Si le champ n'est trouvé dans aucune classe (y compris les classes parentes)
		throw new NoSuchFieldException("Champ '" + fieldName + "' introuvable dans l'objet donné.");
	}
	public void injectMySession(Object target, MySession mySession) {
		Class<?> clazz = target.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (field.getType().equals(MySession.class)) {
				try {
					field.setAccessible(true);
					field.set(target, mySession);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	


	
	public Object getReponse(HttpServletRequest request,HttpServletResponse response,Class classe) throws Exception
	{
		
		
		Parameter[] parameters = action.getParameters();
		Object[] args = new Object[parameters.length];
		String verb=request.getMethod();
		String erreurParametre="Etu 2565 : Tsisy parametre ny methode";
		AllErreur allErreur=new AllErreur();
		
		if (!verb.equalsIgnoreCase(this.getVerb())) {
			String erreurMethod="Tsy mitovy ny verb "+verb+" "+ getVerb();
			response.sendError(405, erreurMethod);
			Exception e=new Exception(erreurMethod);
			throw e;
			
		}
		for (int i = 0; i < parameters.length; i++) {
			
			if (isPrimitiveOrString(parameters[i].getType())) {
				args[i] = request.getParameter(parameters[i].getName());
				if (parameters[i].isAnnotationPresent(Param.class)) {
					Param param = parameters[i].getAnnotation(Param.class);
					String paramName = param.value();
					String paramValue = request.getParameter(paramName);
	
					// For simplicity, assume all parameters are of type String
					args[i] = mamadikaObject(parameters[i].getType(),paramValue);
				}
				else{
					
					response.sendError(500, erreurParametre);
					Exception e=new Exception(erreurParametre);
					throw e;
				}
			}else if(isPrimitiveStringOrArray(parameters[i].getType())){
				args[i] = request.getParameter(parameters[i].getName());
				System.out.println("isPrimitiveStringOrArray");
				if (parameters[i].isAnnotationPresent(Param.class)) {
					Param param = parameters[i].getAnnotation(Param.class);
					String paramName = param.value();
					System.out.println("parametre "+paramName);
					String[] paramValue = request.getParameterValues(paramName);
					// for(String a : paramValue)
					// {
					// 	System.out.println("vdvd" +a);
					// }
	
					// For simplicity, assume all parameters are of type String
					args[i] = mamadikaObject(parameters[i].getType(),paramValue);
					// args[i] = mamadikaObject(int.class,paramValue);

				}
				else{

					response.sendError(500, erreurParametre);
					Exception e=new Exception(erreurParametre);
					throw e;
				}
			}
			else if (parameters[i].getType()==MySession.class) {
				MySession mySession=new MySession(request.getSession());
				args[i]=mySession;
				
			}
			else if (parameters[i].getType()==HttpServletRequest.class) {
				args[i]=request;
				
			}
			else if (parameters[i].getType() == Part.class) {
				if (parameters[i].isAnnotationPresent(Param.class)) {
					Param param = parameters[i].getAnnotation(Param.class);
					String paramName = param.value();
					
					// Assurez-vous que la requête contient le fichier
					Part part = request.getPart(paramName);
					if (part != null) {
						args[i] = part;
					} else {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fichier non trouvé pour: " + paramName);
						throw new ServletException("Fichier non trouvé");
					}
				} else {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètre manquant pour Part");
					throw new ServletException("Paramètre manquant pour Part");
				}
			}
			else if (parameters[i].getType() == Fichier.class) {
				if (parameters[i].isAnnotationPresent(Param.class)) {
					Param param = parameters[i].getAnnotation(Param.class);
					String paramName = param.value();
					
					// Assurez-vous que la requête contient le fichier
					Part part = request.getPart(paramName);
					if (part != null) {
						args[i] = Fichier.convertirPartEnFichier(part);
					} else {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fichier non trouvé pour: " + paramName);
						throw new ServletException("Fichier non trouvé");
					}
				} else {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Paramètre manquant pour Part");
					throw new ServletException("Paramètre manquant pour Part");
				}
			}
			else if (parameters[i].getType() == AllErreur.class) {
				args[i]=allErreur;
			}

			else{
				
				ArrayList<String> listeParametre=makaParametre(request);
				String nomParametre=parameters[i].getName();
				if (parameters[i].isAnnotationPresent(Param.class)) {
					Param param = parameters[i].getAnnotation(Param.class);
					nomParametre = param.value();
					
					
				}
				else{
					response.sendError(500, erreurParametre);
					Exception e=new Exception(erreurParametre);
					throw e;
				}
				Class cl=parameters[i].getType();
				// Employer e=new Employer();
				Object object=cl.getConstructor().newInstance();
			   
				
				// Object p[] = new Object[1];
				
				
				for (String a : listeParametre) {
					
					String[] saraka=a.split("\\.");
					if (saraka.length>1) {
						if (saraka[0].equalsIgnoreCase(nomParametre)) {
							String maj=saraka[1].substring(0, 1).toUpperCase() + saraka[1].substring(1);
							Method m = getMethodByName(cl, "set"+maj);
							Parameter[] pa=m.getParameters();
							String parametre=null;
							Object apidirina=null;
							for (Parameter p: pa) {
								if (p.getType()==Part.class) {
									apidirina=request.getPart(a);
									
									
								}
								else{
									

									parametre=request.getParameter(a);
									if (parametre != null && !parametre.trim().isEmpty()) {
										apidirina=mamadikaObject(p.getType(),parametre);    
									}

								}
								
							}
							
							
							
							m.invoke(object,apidirina);
							Field f=getField(object, saraka[1]);
							Erreur erreur=Validation.getErreurParField(object,f);
							if (erreur==null) {
								erreur=new Erreur(null, parametre);
							}
							allErreur.ajouterListeErreur(a, erreur);


														
						}                        
					}
					// Exception ex=new Exception(saraka[1]);
					// throw ex;


				}
				// HashMap<String,Erreur> listeValidation=Validation.manaoValidation(object);
				// if (listeValidation.size()>0) {
				//     allErreur.ajouterListeErreur(nomParametre, listeValidation);
						
				// }
				
				args[i]=object;
				
				
					

				
			}
			
			
		}
		Object instance = classe.getDeclaredConstructor().newInstance();
		
		MySession mySession=new MySession(request.getSession());
		injectMySession(instance, mySession);
		return action.invoke(instance, args);

	}

	public boolean isPrimitiveStringOrArray(Class<?> paramType) {
		return  paramType.isArray();
	}
	private static Object mamadikaObject(Class<?> clazz, String[] tabs) throws Exception {
        if (tabs == null) {
            return null;
        }
		// System.out.println("ATOOO");


        Object array = Array.newInstance(clazz.getComponentType(), tabs.length);
		System.out.println(tabs.length);

        for (int i = 0; i < tabs.length; i++) {
			// System.out.println(tabs[i]+"KJHGFDFG");
            Object convertedValue = mamadikaObject(clazz.getComponentType(), tabs[i]);
            Array.set(array, i, convertedValue);
        }

        return array;
    }
	
}